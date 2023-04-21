package com.microservice.authservice.controller;

import com.microservice.authservice.dto.PaginatedUserResponse;
import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {


	private final UserService userService;

	/*@PostMapping("/")
	public UserDto addUser(@RequestBody UserDto userDto) {
		return userService.addUser(userDto);
	}*/

	@GetMapping("/")
	public PaginatedUserResponse getAllUsers(Pageable pageable) {
		return userService.getAllUsers(pageable);
	}

	@GetMapping("/{userId}")
	public UserDto getUserById(@PathVariable Integer userId) {
		return userService.getUserById(userId);
	}

	/*@GetMapping("/role/{roleId}")
	public List<UserDto> getUserByRoleId(@PathVariable long roleId) {
		return userService.getUserByRoleId(roleId);
	}*/

	@DeleteMapping("/{userId}")
	public String deleteById(@PathVariable Integer userId) {
		return userService.deleteById(userId);
	}

	@PutMapping("/")
	public UserDto updateUser(@RequestBody UserDto userDto) {
		return userService.updateUser(userDto);
	}

}
