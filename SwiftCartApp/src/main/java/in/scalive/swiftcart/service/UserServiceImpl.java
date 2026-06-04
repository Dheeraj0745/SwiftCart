package in.scalive.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.scalive.swiftcart.dto.request.UpdateUserRequestDTO;
import in.scalive.swiftcart.dto.request.UserRequestDTO;
import in.scalive.swiftcart.dto.response.UserResponseDTO;
import in.scalive.swiftcart.entity.Cart;
import in.scalive.swiftcart.entity.User;
import in.scalive.swiftcart.exception.DuplicateResourceException;
import in.scalive.swiftcart.exception.ResourceNotFoundException;
import in.scalive.swiftcart.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;

	@Autowired
	public UserServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	private UserResponseDTO mapToResponse(User user) {
		return UserResponseDTO.builder().id(user.getId()).fullName(user.getFullName()).email(user.getEmail())
				.phone(user.getPhone()).address(user.getAddress()).isActive(user.getIsActive()).build();
	}

	private User findUserById(Long id) {
		Optional<User> opt = userRepo.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new ResourceNotFoundException("User", "id", id);
	}

	@Override
	public UserResponseDTO createUser(UserRequestDTO userRequest) {
		if (existsByEmail(userRequest.getEmail())) {
			throw new DuplicateResourceException("User", "email", userRequest.getEmail());
		}
		User user = User.builder().fullName(userRequest.getFullName()).email(userRequest.getEmail())
				.password(userRequest.getPassword()).phone(userRequest.getPhone()).address(userRequest.getAddress())
				.build();

		Cart cart = Cart.builder().user(user).build();
		user.setCart(cart);
		User savedUser = userRepo.save(user);
		return mapToResponse(savedUser);
	}

	@Override
	public UserResponseDTO getUserById(Long id) {
		User user = findUserById(id);
		return mapToResponse(user);
	}

	@Override
	public UserResponseDTO getUserByEmail(String email) {
		Optional<User> opt = userRepo.findByEmail(email);
		if (opt.isPresent()) {
			return mapToResponse(opt.get());
		}
		throw new ResourceNotFoundException("User", "email", email);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (User user : users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	@Override
	public List<UserResponseDTO> getActiverUsers() {
		List<User> users = userRepo.findByIsActiveTrue();
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (User user : users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	@Override
	public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userRequest) {
		User user = findUserById(id);
		if (userRequest.getFullName() == null && userRequest.getPassword() == null && userRequest.getPhone() == null
				&& userRequest.getEmail() == null && userRequest.getAddress() == null) {
			throw new IllegalArgumentException("At least one field must be provided for updation");  // sb kuch null hai toh kay update hoga
		}
		if (userRequest.getFullName() != null) {
			if (userRequest.getFullName().isBlank()) {
				throw new IllegalArgumentException("FullName cannot be blank");
			}
			user.setFullName(userRequest.getFullName());
		}
		if (userRequest.getPassword() != null) {
			if (userRequest.getPassword().isBlank()) {
				throw new IllegalArgumentException("Password cannot be blank");
			}
			user.setPassword(userRequest.getPassword());
		}
		if (userRequest.getPhone() != null) {
			if (userRequest.getPhone().isBlank()) {
				throw new IllegalArgumentException("Phone number cannot be blank");
			}
			user.setPhone(userRequest.getPhone());
		}
		if (userRequest.getEmail() != null) {
			if (userRequest.getEmail().isBlank()) {
				throw new IllegalArgumentException("Email cannot be blank");
			}
			//checking existing email
			boolean res=userRepo.existsByEmail(userRequest.getEmail());
			if(res==true) {
				throw new DuplicateResourceException("User","email",userRequest.getEmail());
			}			
			user.setEmail(userRequest.getEmail());
		}
		if (userRequest.getAddress() != null) {
			if (userRequest.getAddress().isBlank()) {
				throw new IllegalArgumentException("Address cannot be blank");
			}
			user.setAddress(userRequest.getAddress());
		}
        User updatedUser= userRepo.save(user);
        return mapToResponse(updatedUser);  
	}

	@Override
	public void activateUser(Long id) {
		User user = findUserById(id);
		user.setIsActive(true);
		userRepo.save(user);
	}

	@Override
	public void deActivateUser(Long id) {
		User user = findUserById(id);
		user.setIsActive(false);
		userRepo.save(user);

	}

	@Override
	public List<UserResponseDTO> searchUser(String keyword) {
		List<User> users = userRepo.searchByNameOrEmail(keyword);
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (User user : users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

}
