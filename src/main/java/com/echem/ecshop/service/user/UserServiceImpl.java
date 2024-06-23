package com.echem.ecshop.service.user;

import com.echem.ecshop.config.PasswordEncoder;
import com.echem.ecshop.dao.UserRepository;
import com.echem.ecshop.domain.ConfirmationToken;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.mapper.UserMapper;
import com.echem.ecshop.service.token.ConfirmationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final String USER_NOT_FOUND_MSG = "user with email %s not found";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;

	private final UserMapper mapper = UserMapper.MAPPER;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.confirmationTokenService = confirmationTokenService;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		return userRepository.findByUsername(userName).orElseThrow(()-> new UsernameNotFoundException("User " + userName + " not found"));
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void save(User user) {
		userRepository.save(user);
		logger.info("User {} saved successfully", user.getUsername());
	}


	@Override
	public UserDTO findByUserName(String username) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> {
					logger.error(String.format(USER_NOT_FOUND_MSG, username));
					return new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
				});
		logger.info("User {} found", username);
		return mapper.fromUser(user);
	}

	@Transactional
	@Override
	public String signUpUser(User user) {
		boolean userExists = userRepository.findUserByEmail(user.getEmail()).isPresent();
		if (userExists){
			logger.error("Email {} already used", user.getEmail());
			throw new IllegalStateException("email already used");
		}
		String encode = passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encode);
		userRepository.save(user);


		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15L),
				user
		);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		logger.info("User {} signed up successfully", user.getUsername());
		return token;
	}

	@Override
	public void enableUser(String userName) {
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> {
            logger.error("{} not found", userName);
			return new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, userName));
		});
		user.setIsEnable(true);
		userRepository.save(user);
		logger.info("User {} enabled successfully", userName);
	}

	@Override
	public User getRefById(Long id) {
		return userRepository.getReferenceById(id);
	}
}