package com.echem.ecshop.service;

import com.echem.ecshop.config.PasswordEncoder;
import com.echem.ecshop.dao.UserRepository;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.registration.token.ConfirmationToken;
import com.echem.ecshop.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	private static final String USER_NOT_FOUND_BSG = "user with email %s not found";
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return findByEmail(email);
	}
	@Override
	public User findByEmail(String email) {
		return userRepository.findUserByEmail(email)
				.orElseThrow(()->new IllegalStateException(
						String.format(USER_NOT_FOUND_BSG,email)
				));
	}
	@Override
	public User findByName(String username) {
		return userRepository.findByFirstName(username);
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