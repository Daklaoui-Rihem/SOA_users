package com.rihem.users_microservice.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rihem.users_microservice.entities.Role;
import com.rihem.users_microservice.entities.User;
import com.rihem.users_microservice.repos.RoleRepository;
import com.rihem.users_microservice.repos.UserRepository;
import com.rihem.users_microservice.service.exceptions.EmailAlreadyExistsException;
import com.rihem.users_microservice.service.exceptions.ExpiredTokenException;
import com.rihem.users_microservice.service.exceptions.InvalidTokenException;
import com.rihem.users_microservice.service.register.RegistationRequest;
import com.rihem.users_microservice.service.register.VerificationToken;
import com.rihem.users_microservice.service.register.VerificationTokenRepository;
import com.rihem.users_microservice.util.EmailSender;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRep;

	@Autowired
	RoleRepository roleRep;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	VerificationTokenRepository verificationTokenRepo;

	@Autowired
	EmailSender emailSender;

	@Override
	public User saveUser(User user) {

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}

	@Override
	public User addRoleToUser(String username, String rolename) {
		User usr = userRep.findByUsername(username);
		Role r = roleRep.findByRole(rolename);

		usr.getRoles().add(r);
		return usr;
	}

	@Override
	public Role addRole(Role role) {
		return roleRep.save(role);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}

	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}

	@Override
	public User registerUser(RegistationRequest request) {
		Optional<User> optionaluser = userRep.findByEmail(request.getEmail());
		if (optionaluser.isPresent())
			throw new EmailAlreadyExistsException("email déjà existant!");

		User newUser = new User();
		newUser.setUsername(request.getUsername());
		newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
		newUser.setEmail(request.getEmail());
		newUser.setEnabled(false);

		// ajouter à newUser le role par défaut USER
		Role r = roleRep.findByRole("USER");
		newUser.setRoles(List.of(r));
		userRep.save(newUser);

		// génére le code secret
		String code = this.generateCode();

		VerificationToken token = new VerificationToken(code, newUser);
		verificationTokenRepo.save(token);

		// envoyer par email pour valider l'email de l'utilisateur
		sendEmailUser(newUser, token.getToken());

		return newUser;
	}

	public String generateCode() {
		Random random = new Random();
		Integer code = 100000 + random.nextInt(900000);

		return code.toString();
	}

	@Override
	public void sendEmailUser(User u, String code) {
		String emailBody = "Bonjour " + "<h1>" + u.getUsername() + "</h1>" +
				" Votre code de validation est " + "<h1>" + code + "</h1>";
		emailSender.sendEmail(u.getEmail(), emailBody);
	}

	@Override
	public User validateToken(String code) {
		VerificationToken token = verificationTokenRepo.findByToken(code);
		if (token == null) {
			throw new InvalidTokenException("Invalid Token");
		}

		User user = token.getUser();
		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			verificationTokenRepo.delete(token);
			throw new ExpiredTokenException("expired Token");
		}
		user.setEnabled(true);
		userRep.save(user);
		return user;
	}

}