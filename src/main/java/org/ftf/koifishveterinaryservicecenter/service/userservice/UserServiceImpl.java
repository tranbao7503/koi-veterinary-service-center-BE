package org.ftf.koifishveterinaryservicecenter.service.userservice;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.AddressNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.RoleException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.repository.*;
import org.ftf.koifishveterinaryservicecenter.service.fileservice.FileUploadService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private final FishRepository fishRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final VeterinarianSlotsRepository veterinarianSlotsRepository;

    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, FileUploadService fileUploadService, UserMapper userMapper, AuthenticationService authenticationService, FishRepository fishRepository, AppointmentRepository appointmentRepository, PaymentRepository paymentRepository, VeterinarianSlotsRepository veterinarianSlotsRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadService = fileUploadService;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
        this.fishRepository = fishRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
        this.veterinarianSlotsRepository = veterinarianSlotsRepository;
    }


    @Override
    public User getUserProfile(Integer userId) {
        return userRepository.findUsersByUserId(userId);
    }

    @Override
    public List<User> getAllVeterinarians() {
        Role role = roleRepository.findByRoleKey("VET");
        List<User> veterinarians = new ArrayList<>(role.getUsers());
        return veterinarians;
    }

    @Override
    @Transactional
    public User updateAddress(Integer userId, Address convertedAddress) {
        User userFromDb = userRepository.findUsersByUserId(userId);

        if (userFromDb == null) {
            throw new UserNotFoundException("Not found user with Id: " + userId);
        }

        // set addressId for Address input
        Integer addressId = userFromDb.getCurrentAddress().getAddressId();
        convertedAddress.setAddressId(addressId);

        // update Address property for User instance
        Address updatedAddress = addressRepository.save(convertedAddress);
        userFromDb.setCurrentAddress(updatedAddress);
        return userFromDb;
    }

    @Override
    @Transactional
    public User updateUserProfile(Integer userId, User convertedCustomer) {
        User userFromDb = userRepository.findUsersByUserId(userId);

        if (userFromDb == null) {
            throw new UserNotFoundException("Not found user with Id: " + userId);
        }

        // set addressId for User input
        Integer customerId = userFromDb.getUserId();
        convertedCustomer.setUserId(userId);

        // fill in empty fields


        // check firstname
        if (convertedCustomer.getFirstName() != null) {
            userFromDb.setFirstName(convertedCustomer.getFirstName());
        }

        // check lastname
        if (convertedCustomer.getLastName() != null) {
            userFromDb.setLastName(convertedCustomer.getLastName());
        }

        // check phone number
        String phoneNumber = convertedCustomer.getPhoneNumber();
        if (!phoneNumber.equals(userFromDb.getPhoneNumber()) && !userRepository.existsUserByPhoneNumber(phoneNumber)) {
            userFromDb.setPhoneNumber(phoneNumber);
        }

        userFromDb = userRepository.save(userFromDb);

        // update user's profile for User instance
        return userFromDb;
    }

    @Override
    public List<User> getAllCustomers() {
        Role role = roleRepository.findByRoleKey("CUS");
        List<User> customers = new ArrayList<>(role.getUsers());
        return customers;
    }

    @Override
    public void signUp(String username, String password, String email, String first_Name, String last_Name) {

        // Kiểm tra username
        if (username == null || username.isBlank()) {
            throw new AuthenticationException("Username can not be empty");
        }
        if (username.contains(" ")) {
            throw new AuthenticationException("Username can not contain white space");
        }
        if (userRepository.findUserByUsername(username) != null) {
            throw new AuthenticationException("Username is existed");
        }


        // Kiểm tra password
        if (password == null || password.isBlank()) {
            throw new AuthenticationException("Password can not be empty");
        }
        if (password.length() < 8) {
            throw new AuthenticationException("Password can not be less than 8 characters");
        }
        String passwordPattern = "^(?=.*[@#$%^&+=!{}]).{8,}$";
        if (!password.matches(passwordPattern)) {
            throw new AuthenticationException("Password must contain at least one special character and be at least 8 characters long");
        }

        // Kiểm tra email
        if (email == null || email.isBlank()) {
            throw new AuthenticationException("Email can not be empty");
        }
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(emailPattern)) {
            throw new AuthenticationException("Email is not valid");
        }
        if (userRepository.findUserByEmail(email) != null) {
            throw new AuthenticationException("Email is already registered");
        }

        // Kiểm tra first_name
        if (first_Name == null || first_Name.isBlank()) {
            throw new AuthenticationException("first_Name can not be empty");
        }

        // Kiểm tra last_name
        if (last_Name == null || last_Name.isBlank()) {
            throw new AuthenticationException("last_Name can not be empty");
        }

        // Tạo user mới và lưu vào database
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findByRoleKey("CUS");
        user.setRole(role);
        user.setFirstName(first_Name);
        user.setLastName(last_Name);
        user.setEmail(email); // Gán email cho user
        userRepository.save(user);
    }


    @Override
    public User getVeterinarianById(Integer veterinarianId) {
        User veterinarian = userRepository.findVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new UserNotFoundException("Veterinarian not found with Id: " + veterinarianId);
        }
        return veterinarian;
    }

    @Override
    public User getCustomerById(Integer customerId) {
        User customer = userRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new UserNotFoundException("Customer not found with Id: " + customerId);
        }
        return customer;
    }


    public UserDTO createStaff(String userName, String passWord, String firstName, String lastName) {
        User user = new User();

        user.setUsername(userName);
        user.setPassword(passWord);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(roleRepository.findByRoleKey("STA")); // Set role trực tiếp tại đây
        // encrypt password
        userRepository.save(user);

        // Sử dụng MapStruct để chuyển đổi từ User sang UserDTO
        return userMapper.convertEntityToDto(user); // Giả sử userMapper là một instance của MapStruct
    }

    @Override
    public User updateUserAvatar(Integer userId, MultipartFile image) throws IOException {
        User user = userRepository.findUsersByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("Not found user with Id: " + userId);
        }
        String path = fileUploadService.uploadFile(image);
        user.setAvatar(path);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<Address> getAllAddresses(Integer customerId) {
        List<Address> addresses = addressRepository.findByCustomerId(customerId);
        if (addresses.isEmpty()) {
            throw new AddressNotFoundException("Address not found with customer ID: " + customerId);
        }
        return addresses;
    }

    @Override
    public Address getAddressById(Integer addressId) {
        Address address = addressRepository.findById(addressId).orElse(null);
        if (address == null) {
            throw new AddressNotFoundException("Address not found with ID: " + addressId);
        }
        return address;
    }

    @Override
    public Address updateAddressDetails(Integer addressId, Address newAddress) {
        Address existedAddress = addressRepository.findById(addressId).orElse(null);
        if (existedAddress == null) {
            throw new AddressNotFoundException("Address not found with ID: " + addressId);
        } else {
            existedAddress.setCity(newAddress.getCity());
            existedAddress.setWard(newAddress.getWard());
            existedAddress.setDistrict(newAddress.getDistrict());
            existedAddress.setHomeNumber(newAddress.getHomeNumber());

            newAddress = addressRepository.save(existedAddress);

            return newAddress;
        }
    }

    @Override
    public Address setCurrentAddress(Integer customerId, Integer addressId) throws UserNotFoundException {
        User customer = this.getCustomerById(customerId);
        Address address = addressRepository.findById(addressId).orElse(null);
        if (address == null) {
            throw new AddressNotFoundException("Address not found with ID: " + addressId);
        } else {
            customer.setCurrentAddress(address);
            userRepository.save(customer);
            return address;
        }
    }

    @Override
    public Address addAddress(Integer customerId, Address address) throws UserNotFoundException {
        User customer = this.getCustomerById(customerId);

        // Save address into database
        address.setEnabled(true);
        address.setCustomer(customer);
        address = addressRepository.save(address);

        // Set new address as main current address
        this.setCurrentAddress(customerId, address.getAddressId());

        return address;
    }

    @Override
    public Address disableAddress(Integer addressId) {
        Address address = addressRepository.findById(addressId).orElse(null);
        if (address == null) {
            throw new AddressNotFoundException("Address not found with ID: " + addressId);
        }
        address.setEnabled(false);
        address = addressRepository.save(address);
        return address;
    }

    @Override
    public List<User> getAllStaffs() {
        // Lấy danh sách staffs dựa vào Role
        Role staffRole = roleRepository.findByRoleKey("STA");

        // Kiểm tra nếu role không tồn tại
        if (staffRole == null) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không tìm thấy role
        }

        // Trả về danh sách users từ role "STA"
        return new ArrayList<>(staffRole.getUsers());
    }

    @Override
    public UserDTO updateUserInfo(int userId, boolean enabled) {
        // Lấy thông tin người dùng từ database
        User userFromDb = userRepository.findById(userId).orElse(null);


        if (userFromDb == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        Role role = userFromDb.getRole();
        if (role.getRoleId() == 1 || role.getRoleId() == 2) {
            throw new RoleException("This role don't need to change status");
        }

        // Cập nhật thông tin người dùng
        userFromDb.setEnabled(enabled);

        // Lưu người dùng đã cập nhật
        User updatedUser = userRepository.save(userFromDb);

        // Sử dụng mapper để chuyển đổi entity sang DTO
        return userMapper.convertEntityToDto(updatedUser);
    }



    @Override
    public UserDTO updatePassword(String newPassword) {
        // Lấy userId từ token
        int userId = authenticationService.getAuthenticatedUserId();

        User userFromDb = userRepository.findUsersByUserId(userId);

        if (userFromDb == null) {
            throw new UserNotFoundException("Không tìm thấy người dùng với Id: " + userId);
        }

        // Kiểm tra mật khẩu mới không được null hoặc trống
        if (newPassword == null || newPassword.isBlank()) {
            throw new AuthenticationException("Mật khẩu không được để trống");
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 8) {
            throw new AuthenticationException("Mật khẩu không được ngắn hơn 8 ký tự");
        }

        // Kiểm tra mật khẩu có chứa ít nhất một ký tự đặc biệt
        String passwordPattern = "^(?=.*[@#$%^&+=!{}]).{8,}$";
        if (!newPassword.matches(passwordPattern)) {
            throw new AuthenticationException("Mật khẩu phải chứa ít nhất một ký tự đặc biệt và có độ dài tối thiểu là 8 ký tự");
        }

        // Mã hóa mật khẩu mới và cập nhật
        userFromDb.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userFromDb);

        // Chuyển đổi User sang UserDTO
        return UserMapper.INSTANCE.convertEntityToDto(userFromDb); // Giả sử bạn có một mapper cho User
    }

    @Override
    public Map<String, String> getUserAndFishStatistics() {
        Map<String, String> statistics = new HashMap<>();

        long totalFish = fishRepository.countEnabledFish();
        long totalStaff = userRepository.countEnabledStaff();
        long totalVets = userRepository.countEnabledVets();
        long totalCustomers = userRepository.countEnabledCustomers();

        statistics.put("totalFish", String.valueOf(totalFish));
        statistics.put("totalStaff", String.valueOf(totalStaff));
        statistics.put("totalVets", String.valueOf(totalVets));
        statistics.put("totalCustomers", String.valueOf(totalCustomers));

        return statistics;
    }

    @Override
    public Map<String, String> getAppointmentStatistics() {
        Map<String, String> appointmentStatistics = new HashMap<>();

        // Tính số lượng cuộc hẹn
        long totalAppointments = appointmentRepository.count();
        long totalAppointmentsToday = appointmentRepository.countAppointmentsToday();

        // Tính số lượng cuộc hẹn theo từng dịch vụ
        long service1Appointments = appointmentRepository.countByService_ServiceId(1);
        long service2Appointments = appointmentRepository.countByService_ServiceId(2);
        long service3Appointments = appointmentRepository.countByService_ServiceId(3);
        long taikhamAppointments = appointmentRepository.countByService_ServiceId(4);

        // Tính số lượng cuộc hẹn theo từng dịch vụ trong ngày hôm nay
        long service1AppointmentsToday = appointmentRepository.countByService_ServiceIdToday(1);
        long service2AppointmentsToday = appointmentRepository.countByService_ServiceIdToday(2);
        long service3AppointmentsToday = appointmentRepository.countByService_ServiceIdToday(3);
        long taikhamAppointmentsToday = appointmentRepository.countByService_ServiceIdToday(4);

        // Thêm các giá trị vào map appointmentStatistics
        appointmentStatistics.put("totalAppointments", String.valueOf(totalAppointments));
        appointmentStatistics.put("totalAppointmentsToday", String.valueOf(totalAppointmentsToday));
        appointmentStatistics.put("service1Appointments", String.valueOf(service1Appointments));
        appointmentStatistics.put("service2Appointments", String.valueOf(service2Appointments));
        appointmentStatistics.put("service3Appointments", String.valueOf(service3Appointments));
        appointmentStatistics.put("taikhamAppointments", String.valueOf(taikhamAppointments));
        appointmentStatistics.put("service1AppointmentsToday", String.valueOf(service1AppointmentsToday));
        appointmentStatistics.put("service2AppointmentsToday", String.valueOf(service2AppointmentsToday));
        appointmentStatistics.put("service3AppointmentsToday", String.valueOf(service3AppointmentsToday));
        appointmentStatistics.put("taikhamAppointmentsToday", String.valueOf(taikhamAppointmentsToday));

        return appointmentStatistics;
    }

    @Override
    public Map<String, String> getPaymentStatistics() {
        Map<String, String> paymentStatistics = new HashMap<>();

        // Tính số lượng thanh toán
        long totalPayments = paymentRepository.count();
        long totalPaymentsToday = paymentRepository.countPaymentsToday();

        // Tính tổng số tiền thanh toán và tổng tiền thanh toán trong ngày
        double totalAmountToday = (paymentRepository.sumTotalAmountToday() != null) ? paymentRepository.sumTotalAmountToday() : 0.0;
        double totalAmount = (paymentRepository.sumTotalAmount() != null) ? paymentRepository.sumTotalAmount() : 0.0;

        // Tính số lượng thanh toán theo phương thức
        long cashPayments = paymentRepository.countByPaymentMethod(PaymentMethod.CASH);
        long vnPayPayments = paymentRepository.countByPaymentMethod(PaymentMethod.VN_PAY);

        long cashPaymentsToday = paymentRepository.countByPaymentMethodToday(PaymentMethod.CASH);
        long vnPayPaymentsToday = paymentRepository.countByPaymentMethodToday(PaymentMethod.VN_PAY);

        // Thống kê theo trạng thái "PAID" và "NOT_PAID"
        long paidPayments = paymentRepository.countByStatus(PaymentStatus.PAID);
        long notPaidPayments = paymentRepository.countByStatus(PaymentStatus.NOT_PAID);

        long paidPaymentsToday = paymentRepository.countByStatusToday(PaymentStatus.PAID);
        long notPaidPaymentsToday = paymentRepository.countByStatusToday(PaymentStatus.NOT_PAID);

        // Thêm các giá trị vào map paymentStatistics
        paymentStatistics.put("totalPayments", String.valueOf(totalPayments));
        paymentStatistics.put("totalPaymentsToday", String.valueOf(totalPaymentsToday));
        paymentStatistics.put("totalAmount", String.valueOf(totalAmount));
        paymentStatistics.put("totalAmountToday", String.valueOf(totalAmountToday));

        // Thêm thống kê theo phương thức thanh toán
        paymentStatistics.put("cashPayments", String.valueOf(cashPayments));
        paymentStatistics.put("vnPayPayments", String.valueOf(vnPayPayments));
        paymentStatistics.put("cashPaymentsToday", String.valueOf(cashPaymentsToday));
        paymentStatistics.put("vnPayPaymentsToday", String.valueOf(vnPayPaymentsToday));

        // Thống kê theo trạng thái
        paymentStatistics.put("paidPayments", String.valueOf(paidPayments));
        paymentStatistics.put("notPaidPayments", String.valueOf(notPaidPayments));
        paymentStatistics.put("paidPaymentsToday", String.valueOf(paidPaymentsToday));
        paymentStatistics.put("notPaidPaymentsToday", String.valueOf(notPaidPaymentsToday));

        return paymentStatistics;
    }

    @Override
    public long getVetSlotsInCurrentWeek(int vetId) {
        LocalDate today = LocalDate.now();

        // Tính ngày đầu tuần (Thứ Hai)
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        // Tính ngày cuối tuần (Chủ Nhật)
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // Lấy năm và tháng của ngày bắt đầu
        int year = startOfWeek.getYear();
        int month = startOfWeek.getMonthValue();
        int startDay = startOfWeek.getDayOfMonth();
        int endDay = endOfWeek.getDayOfMonth();

        // Gọi phương thức đếm slots của bác sĩ
        return veterinarianSlotsRepository.countSlotsByVetInDateRange(vetId, year, month, startDay, endDay);
    }



}









