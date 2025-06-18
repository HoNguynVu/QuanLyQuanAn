# ĐỒ ÁN MÔN HỌC SE114
<p align="center">
  <a href="https://www.uit.edu.vn/" title="Trường Đại học Công nghệ Thông tin" style="border: 5;">
    <img src="https://i.imgur.com/WmMnSRt.png" alt="Trường Đại học Công nghệ Thông tin | University of Information Technology">
  </a>
</p>
<h1 align="center"><b><i>NHẬP MÔN ỨNG DỤNG DI ĐỘNG - SE114</i></b>

## BẢNG MỤC LỤC

- [ Giới thiệu môn học](#gioithieumonhoc)
- [ Giảng viên hướng dẫn](#giangvien)
- [ Thành viên nhóm](#thanhvien)
- [ Sản phẩm môn học](#sanpham)
- [ Mô tả tổng quát](#motatq)
- [ Mô tả chi tiết](#motact)
- [ Hướng dẫn cài đặt](#hd)

## GIỚI THIỆU MÔN HỌC
<a name="gioithieumonhoc"></a>

- **Tên môn học**: Nhập môn ứng dụng di động
- **Mã môn học**: SE114
- **Lớp học**: SE114.P22

## GIẢNG VIÊN HƯỚNG DẪN
<a name="giangvien"></a>

 - ThS. Nguyễn Tấn Toàn 

## THÀNH VIÊN NHÓM

<a name ="thanhvien"></a>

| STT | MSSV     | Họ và tên         | Email                  |
| --- | -------- | ----------------- | ---------------------- |
| 1   | 23521805 | Hồ Nguyên Vũ      | 23521805@gm.uit.edu.vn |
| 2   | 23521792 | Nguyễn Quang Vinh | 23521792@gm.uit.edu.vn |
| 3   | 23521136 | Nguyễn Văn Phẩm   | 23521136@gm.uit.edu.vn |
| 4   | 23521189 | Triệu Đại Phú     | 23521189@gm.uit.edu.vn |

## SẢN PHẨM MÔN HỌC : ỨNG DỤNG QUẢN LÝ QUÁN ĂN
<a name="sanpham"></a>

### ĐẶT VẤN ĐỀ
- Hiện nay, việc quản lý hoạt động kinh doanh trong các quán ăn, đặc biệt là các quán ăn nhỏ và vừa, đang đối mặt với nhiều khó khăn. Các nghiệp vụ như đặt món, ghi nhận đơn hàng, tính toán doanh thu, quản lý thực đơn hay kiểm soát tồn kho thường được thực hiện thủ công hoặc thông qua những phương pháp không chuyên nghiệp như ghi chép tay, bảng Excel đơn lẻ. Điều này dẫn đến nguy cơ sai sót trong quá trình vận hành, tốn nhiều thời gian, công sức và thiếu tính hệ thống trong quản lý.
- Trong bối cảnh đó, nhu cầu xây dựng một ứng dụng quản lý quán ăn thông minh, tiện lợi và hiệu quả trở nên ngày càng cấp thiết. Ứng dụng không chỉ giúp đơn giản hóa quy trình đặt món, quản lý đơn hàng và theo dõi doanh thu, mà còn tạo ra một môi trường làm việc chuyên nghiệp, góp phần nâng cao trải nghiệm của cả người bán và khách hàng.

### CÔNG CỤ SỬ DỤNG

#### Frontend (Android)
- **Language**: Java
- **IDE**: Android Studio
- **Build Tool**: Gradle
- **Libraries**:
  - Retrofit 2.9.0 (API calls)
  - OkHttp 4.12.0 (HTTP client)
  - Gson 2.10.1 (JSON parsing)
  - Glide (Image loading)
  - MPAndroidChart v3.1.0 (Charts)
  - Volley 1.2.1 (Networking)

#### Backend
- **Language**: PHP
- **Database**: MySQL
- **Server**: Apache (XAMPP)
- **API**: RESTful API

#### Tools
- **Version Control**: Git
- **IDE**: Visual Studio Code, Android Studio

## MÔ TẢ TỔNG QUÁT
<a name="motatq"></a>

### Tính năng chính

#### Dành cho Khách hàng (User)
- Đăng ký/Đăng nhập tài khoản
- Xem thực đơn và chi tiết món ăn
- Đặt hàng và quản lý giỏ hàng
- Xem lịch sử đơn hàng
- Đánh giá và bình luận món ăn
- Quản lý thông tin cá nhân
- Nhận thông báo

#### Dành cho Quản trị viên (Admin)
- Quản lý thực đơn (thêm, sửa, xóa món ăn)
- Quản lý đơn hàng
- Thống kê doanh thu
- Quản lý thông tin cá nhân
- Xem báo cáo và biểu đồ

## MÔ TẢ CHI TIẾT
<a name="motact"></a>

### Kiến trúc ứng dụng
- **Pattern**: MVC (Model-View-Controller)
- **Architecture**: Client-Server
- **Communication**: RESTful API

### Cấu trúc dự án
```
app/
├── src/main/java/com/example/doan/
│   ├── AdminActivity/          # Các Activity dành cho Admin
│   ├── AdminFragment/          # Các Fragment dành cho Admin
│   ├── UserActivity/           # Các Activity dành cho User
│   ├── UserFragment/           # Các Fragment dành cho User
│   ├── ProfileUser/            # Quản lý profile user
│   ├── Login/                  # Đăng nhập/Đăng ký
│   ├── DatabaseClass/          # Model classes
│   ├── DatabaseClassResponse/  # Response models
│   ├── Network/               # API services
│   └── Adapter/               # RecyclerView adapters
└── src/main/res/              # Resources (layouts, drawables, etc.)
```

### API Endpoints
- **Authentication & User Management**
  - `/restaurantapi/login.php`: Đăng nhập
  - `/restaurantapi/signup.php`: Đăng ký tài khoản
  - `/restaurantapi/verify_otp.php`: Xác thực OTP đăng ký
  - `/restaurantapi/forgot_password.php`: Quên mật khẩu
  - `/restaurantapi/verify_reset_password.php`: Xác thực OTP reset mật khẩu
  - `/restaurantapi/reset_password.php`: Đặt lại mật khẩu
  - `/restaurantapi/change_password.php`: Đổi mật khẩu
- **Food Management**
  - `/restaurantapi/get_foods.php`: Lấy danh sách món ăn
  - `/restaurantapi/get_foods_by_category.php`: Lấy món ăn theo danh mục
  - `/restaurantapi/get_foods_by_id.php`: Lấy thông tin món ăn theo ID
  - `/restaurantapi/add_food.php`: Thêm món ăn mới
  - `/restaurantapi/update_food.php`: Cập nhật thông tin món ăn
  - `/restaurantapi/delete_food.php`: Xóa món ăn
- **Order Management**
  - `/restaurantapi/create_order.php`: Tạo đơn hàng mới
  - `/restaurantapi/get_orders.php`: Lấy danh sách đơn hàng (Admin)
  - `/restaurantapi/get_orders_by_user.php`: Lấy đơn hàng theo user
  - `/restaurantapi/get_order_items.php`: Lấy chi tiết món trong đơn hàng
  - `/restaurantapi/update_order_status.php`: Cập nhật trạng thái đơn hàng
- **Reviews System**
  - `/restaurantapi/add_review.php`: Thêm đánh giá
  - `/restaurantapi/get_reviews_by_food_id.php`: Lấy đánh giá theo món ăn
- **Notification System**
  - `/restaurantapi/create_notification.php`: Tạo thông báo
  - `/restaurantapi/add_notification.php`: Thêm thông báo
  - `/restaurantapi/get_notifications.php`: Lấy danh sách thông báo
- **User Profile Management**
  - `/restaurantapi/get_user_by_id.php`: Lấy thông tin user theo ID
  - `/restaurantapi/update_user.php`: Cập nhật thông tin user
  - `/restaurantapi/get_admin_info.php`: Lấy thông tin admin
  - `/restaurantapi/update_admin_info.php`: Cập nhật thông tin admin
- **Statistics and Analytics**
  - `/restaurantapi/get_statistics.php`: Lấy thống kê doanh thu
- **System and Database**
  - `/restaurantapi/db.php`: File kết nối database
- **Email Service**
  - Thư mục `PHPMailer/`: Thư viện gửi email cho OTP và thông báo 

### Tính năng nổi bật
1. **Xác thực OTP qua email** khi đăng ký
2. **Upload ảnh** cho món ăn với xử lý Base64
3. **Biểu đồ thống kê** doanh thu bằng MPAndroidChart
4. **Mã giảm giá** cho đơn hàng
5. **Hệ thống đánh giá** 5 sao cho món ăn
6. **Thông báo** realtime cho người dùng

## HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY ỨNG DỤNG
<a name="hd"></a>

### Yêu cầu hệ thống

#### Phần mềm cần thiết
- **Android Studio**: Phiên bản 2023.1.1 trở lên
- **Java Development Kit (JDK)**: JDK 8 trở lên
- **XAMPP**: Để chạy Apache server và MySQL
- **Git**: Để clone repository

### Bước 1: Cài đặt Backend (Server)

#### 1.1 Cài đặt XAMPP
1. Tải và cài đặt XAMPP từ [https://www.apachefriends.org/](https://www.apachefriends.org/)
2. Khởi động XAMPP Control Panel
3. Start **Apache** và **MySQL**

#### 1.2 Thiết lập Database
1. Mở trình duyệt và truy cập `http://localhost/phpmyadmin`
2. Tạo database mới có tên `restaurant_db`
3. Import file SQL từ thư mục `/database` trong repository

#### 1.3 Upload API files
1. Copy thư mục `restaurantapi` vào `C:\xampp\htdocs\`
2. Cấu trúc thư mục:
```
C:\xampp\htdocs\restaurantapi\
├── login.php
├── register.php
├── getFoods.php
├── getOrders.php
├── update_order_status.php
└── ... (các file API khác)
```

#### 1.4 Cấu hình kết nối Database
Chỉnh sửa file `db.php` trong thư mục `restaurantapi`:
```php
<?php
$servername = "localhost";
$username = "root";
$password = ""; //điền mật khẩu của bạn
$dbname = "restaurant_db";
?>
```

### Bước 2: Cài đặt Frontend (Android App)

#### 2.1 Clone Repository
```bash
git clone [https://github.com/HoNguynVu/QuanLyQuanAn]
cd QuanLyQuanAn
```

#### 2.2 Mở project trong Android Studio
1. Mở Android Studio
2. Chọn **File** → **Open**
3. Chọn thư mục `QuanLyQuanAn`
4. Chờ Android Studio sync project

#### 2.3 Cấu hình API Base URL
Mở file `RetrofitClient.java` và cập nhật BASE_URL:
```java
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2/restaurantapi/";
    // Hoặc sử dụng IP thực của máy: "http://192.168.1.100/restaurantapi/"
}
```

**Lưu ý về IP:**
- **Emulator**: Sử dụng `10.0.2.2` thay cho `localhost`
- **Thiết bị thật**: Sử dụng IP thực của máy tính (vd: `192.168.1.100`)

#### 2.4 Sync Dependencies
1. Mở file `build.gradle (Module: app)`
2. Kiểm tra các dependencies đã được khai báo
3. Nhấn **Sync Now** nếu Android Studio yêu cầu

### Bước 3: Chạy ứng dụng

#### 3.1 Chạy trên Emulator
1. Trong Android Studio, nhấn **AVD Manager**
2. Tạo Virtual Device mới
3. Khởi động Emulator
4. Nhấn nút **Run** (▶️) hoặc `Shift + F10`

#### 3.2 Chạy trên thiết bị thật
1. Bật **Developer Options** trên thiết bị Android
2. Bật **USB Debugging**
3. Kết nối thiết bị với máy tính qua USB
4. Chọn thiết bị trong Android Studio
5. Nhấn nút **Run** (▶️)

### Bước 4: Kiểm tra kết nối

#### 4.1 Test API
Mở trình duyệt và test các endpoint:
- `http://localhost/restaurantapi/db.php`
- `http://localhost/restaurantapi/login.php`

#### 4.2 Test App
1. Mở ứng dụng trên thiết bị/emulator
2. Thử đăng ký tài khoản mới
3. Đăng nhập và kiểm tra các tính năng

### Xử lý lỗi thường gặp

#### Lỗi kết nối API
```
Solution: Kiểm tra lại BASE_URL và đảm bảo XAMPP đang chạy
```

#### Lỗi Sync Gradle
```bash
./gradlew clean
./gradlew build
```

#### Lỗ Database Connection
```
Solution: Kiểm tra cấu hình database trong db.php
```

#### Lỗi Network Security (API 28+)
Thêm vào `AndroidManifest.xml`:
```xml
<application
    android:usesCleartextTraffic="true"
    ... >
```

**Lưu ý**: Đảm bảo tất cả services (Apache, MySQL) đang chạy trước khi test ứng dụng.

