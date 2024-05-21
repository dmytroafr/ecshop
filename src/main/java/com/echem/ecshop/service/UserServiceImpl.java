package com.echem.ecshop.service;

import com.echem.ecshop.config.PasswordEncoder;
import com.echem.ecshop.dao.UserRepository;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.mapper.UserMapper;
import com.echem.ecshop.registration.token.ConfirmationToken;
import com.echem.ecshop.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	private static final String USER_NOT_FOUND_BSG = "user with email %s not found";
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private final UserMapper mapper = UserMapper.MAPPER;
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		return findByUserName(userName);
	}
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	@Override
	public void save(User user) { userRepository.save(user);}
	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName)
				.orElseThrow(()->new IllegalStateException(
						String.format(USER_NOT_FOUND_BSG,userName)
				));
	}
	@Override
	public User findByName(String username) {
		return userRepository.findUserByEmail(username).orElseThrow(
				()->new IllegalStateException("user "+username+" not found ")
		);
	}

	@Override
	public String signUpUser(User user) {
		boolean present = userRepository.findUserByEmail(user.getEmail()).isPresent();
		if (present){
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
		return token;
	}

	@Override
	public void enableUser(String email) {
		User user = userRepository.findUserByEmail(email).orElseThrow();
		user.setIsEnable(true);
	}
}