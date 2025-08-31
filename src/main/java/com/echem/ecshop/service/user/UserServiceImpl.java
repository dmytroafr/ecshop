package com.echem.ecshop.service.user;

import com.echem.ecshop.dao.UserRepository;
import com.echem.ecshop.domain.ConfirmationToken;
import com.echem.ecshop.domain.Role;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.mapper.UserMapper;
import com.echem.ecshop.service.bucket.BucketService;
import com.echem.ecshop.service.token.ConfirmationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final ConfirmationTokenService confirmationTokenService;
	private final BucketService bucketService;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper mapper = UserMapper.MAPPER;

	public UserServiceImpl(UserRepository userRepository, ConfirmationTokenService confirmationTokenService, BucketService bucketService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.confirmationTokenService = confirmationTokenService;
		this.bucketService = bucketService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDTO getUserDTOByUserName(String username) {
		log.debug("Method getUserDTOByUserName({}) called",username);
		User user = getUserByUsername(username);
		log.info("User {} found -> mapping to DTO", username);
		return mapper.fromUser(user);
	}

	@Override
	public UserDTO getUserDTOByEmail(String email) {
		log.debug("Method getUserDTOByEmail({}) called",email);
		User user = getUserByEmail(email);
		log.info("User with email {} found -> mapping to DTO", email);
		return mapper.fromUser(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("UserDetails method loadUserByUsername({}) called, returning UserDetails",username);
		return userRepository
				.findUserByUsername(username)
				.orElseThrow(
						()-> {
							log.error("UserDetails with username {} not found", username);
							return new UsernameNotFoundException("User with username " + username + " not found");
						});
	}

	@Override
	public User getUserByUsername(String username) {
		log.debug("Method getUserByUsername({}) called",username);
		return userRepository.
				findUserByUsername(username)
				.orElseThrow(
						()-> {
							log.error("User with username {} not found", username);
							return new UsernameNotFoundException("User with username " + username + " not found");
						});
	}

	@Transactional
	@Override
	public String signUpUser(RegistrationRequest request) {
		log.debug("Method signUpUser({})", request.username());
		boolean userExists = userRepository.findUserByEmail(request.email()).isPresent();
		if (userExists){
			log.error("Email {} from registration request already used", request.email());
			throw new IllegalStateException("Email already used");
		}
		String encodedPassword = passwordEncoder.encode(request.password());
		User user = new User(
				request.username(),
				encodedPassword,
				request.email(),
				Role.ROLE_CLIENT);
		save(user);
		log.info("New User with username {} created and saved into DB", user.getUsername());

		String token = UUID.randomUUID().toString();
		log.info("Token {} created", token);
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15L),
				user
		);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		log.info("Confirmation token for User {} was created and saved", user.getUsername());
		return token;
	}

	@CachePut(value = "users", key = "#user.username")
	private User save(User user) {
		log.info("Saving User {}", user.getUsername());
		return userRepository.save(user);
	}

	@Override
	public void enableUser(String username) {
		User user = getUserByUsername(username);
		user.setEnabled(true);
		log.info("User with username {} set enabled successfully", username);
		save(user);
		bucketService.createBucket(user);
		log.info("For User with username {} were created the bucket", username);

	}

	@Override
	public Map<String, String> getUserDetailsMap(String username) {
		log.debug("Method getUserDetailsMap({})", username);
		UserDTO userDTO = getUserDTOByUserName(username);
		return userDTO.getMap();
	}

	@Override
	public List<UserDTO> getUsers() {
		log.debug("Method getUsers() called");
		List<User> all = userRepository.findAll();
        return all.stream().map(mapper::fromUser).toList();
	}

	private User getUserByEmail (String email){
		log.debug("Method getUserByEmail({})", email);
		return userRepository
				.findUserByEmail(email)
				.orElseThrow(
						()-> {
							log.error("User with email {} not found", email);
							return new UsernameNotFoundException("User with email " + email + " not found");
						});
	}


}