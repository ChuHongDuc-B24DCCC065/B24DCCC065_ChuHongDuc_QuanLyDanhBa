# BÁO CÁO BÀI TẬP LỚN LẬP TRÌNH ANDROID
## ĐỀ TÀI: ỨNG DỤNG QUẢN LÝ DANH BẠ LIÊN HỆ (QUAN LY DANH BA)

---

### THÔNG TIN SINH VIÊN
- **Họ và tên:** [Điền họ và tên của bạn]
- **Mã số sinh viên (MSSV):** [Điền MSSV của bạn]
- **Lớp:** [Điền tên lớp của bạn]
- **Môn học:** Lập trình di động (Android)
- **Link Git Repository:** https://github.com/[username]/MSSV_HoTen_QuanLyDanhBa

---

## 1. TỔNG QUAN VỀ DỰ ÁN

### 1.1. Mục tiêu bài toán
Xây dựng ứng dụng Android hoàn chỉnh bằng **Java** và **XML layout** trên **Android Studio** với ngôn ngữ thiết kế hiện đại, tối giản và tinh tế:
1. **LoginActivity (Đăng nhập):** Xác thực tài khoản cố định (`admin` / `123456`), giao diện thẻ thương hiệu `[Danh bạ]`, khẩu hiệu *"Kết nối gần hơn."*, input bo góc mềm không viền, báo lỗi tinh gọn kèm icon đỏ.
2. **MainActivity (Danh sách liên hệ):** Header trắng với avatar profile, khối thống kê tối giản kiêm **Tab lọc thông minh** (`[X] Liên hệ` và `[Y] Yêu thích`), **thanh tìm kiếm thời gian thực (Search bar)**, danh sách liên hệ thẻ trắng tối giản với avatar tròn màu pastel dịu mắt, dấu sao teal yêu thích, và nút bấm nổi **Floating Action Button (+)** ở góc dưới bên phải.
3. **AddContactActivity (Thêm liên hệ):** Nút mũi tên quay lại và nút "Hủy" chữ xám ở góc trên, các ô nhập liệu dạng filled light gray, Spinner nhóm, chọn Giới tính dạng **Segmented Control 3 tab (Nam | Nữ | Khác)**, Checkbox yêu thích kèm sao, ô ghi chú nhiều dòng và nút "Lưu liên hệ".
4. **EditContactActivity (Chỉnh sửa liên hệ):** Cho phép sửa toàn diện thông tin liên hệ (họ tên, SĐT, nhóm, giới tính, yêu thích, ghi chú) kèm validation không trùng với số điện thoại của người khác.
5. **ContactDetailActivity (Chi tiết liên hệ):** Avatar tròn pastel lớn ở giữa kèm tên và số điện thoại, nút bấm sao "☆ Yêu thích" đổi trạng thái trực tiếp, **hàng 3 nút hành động tròn (Gọi điện | Nhắn tin | Chỉnh sửa)**, thẻ thông tin chia 2 cột cho Nhóm & Giới tính, thẻ riêng cho Ghi chú, và nút đỏ "Xóa liên hệ" với biểu tượng thùng rác ở đáy màn hình.

### 1.2. Công nghệ và kỹ thuật áp dụng
- **Ngôn ngữ:** Java (JDK 8/17/25).
- **Giao diện:** XML Layout hiện đại, màu sắc chủ đạo xanh Teal/Emerald (`#0D9488` / `#059669`), nền trắng `#FFFFFF`, input filled `#F3F4F6`, avatar pastel (`#D1FAE5`, `#EDE9FE`, `#DBEAFE`).
- **Điều hướng & truyền dữ liệu:** Sử dụng `Intent` tường minh, `Intent` ngầm định (`ACTION_DIAL`, `ACTION_SENDTO`), `ActivityResultLauncher` để nhận kết quả phản hồi (`setResult`).
- **Mô hình dữ liệu:** Lớp `Contact` implements `Serializable`.
- **Quản lý dữ liệu:** Lưu trữ trong bộ nhớ bằng `ArrayList<Contact>` qua lớp singleton `ContactManager`, có sẵn 3 liên hệ mẫu ban đầu đúng theo yêu cầu đề bài.

---

## 2. NỘI DUNG CHI TIẾT CÁC MÀN HÌNH

### 2.1. Màn hình 1: Đăng nhập (LoginActivity)

#### A. Hình ảnh minh chứng
*(Sinh viên chạy ứng dụng và chụp 2 ảnh: màn hình ban đầu và màn hình khi báo lỗi)*

> **[HÌNH ẢNH 1.1: Giao diện màn hình đăng nhập mới]**  
> *(Dán ảnh chụp màn hình LoginActivity tại đây)*

> **[HÌNH ẢNH 1.2: Báo lỗi khi sai thông tin hoặc để trống]**  
> *(Dán ảnh chụp thông báo lỗi đỏ kèm icon chấm than tròn: "Sai tên đăng nhập hoặc mật khẩu")*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện:**
  - Huy hiệu nhỏ góc trên bên trái: `[ 📇 Danh bạ ]` với nền xanh ngọc nhạt bo viền mềm mại.
  - Tiêu đề chữ đậm lớn: **"Kết nối gần hơn."** và dòng phụ đề "Đăng nhập để tiếp tục".
  - Ô nhập `Tên đăng nhập` và `Mật khẩu`: thiết kế dạng hình chữ nhật bo góc mềm 12dp không viền cứng, màu nền xám nhẹ `#F3F4F6`, hiển thị gợi ý mờ.
  - Ô Mật khẩu có thuộc tính `inputType="textPassword"` để che giấu mật khẩu dạng dấu chấm.
  - Dòng thông báo lỗi: gồm biểu tượng chấm than tròn đỏ và dòng chữ đỏ `Sai tên đăng nhập hoặc mật khẩu`, mặc định ẩn (`View.GONE`) và chỉ xuất hiện khi kiểm tra không đạt.
  - Nút "Đăng nhập" chữ trắng in đậm, nền xanh teal bo góc 14dp với hiệu ứng chạm ripple.
  - Phần chân trang (footer) hiển thị tài khoản mẫu: `Tài khoản mẫu / admin / 123456`.
- **Xử lý sự kiện trong code Java:**
  - Kiểm tra rỗng `isEmpty()`: thông báo yêu cầu nhập đầy đủ.
  - So sánh chuỗi với tài khoản cố định `admin / 123456`.
  - Đúng tài khoản: mở `MainActivity` bằng `Intent`, gọi `finish()` để giải phóng activity khỏi Back stack.
  - Sai tài khoản: kích hoạt hiển thị `layoutError` và `Toast`.

---

### 2.2. Màn hình 2: Danh sách liên hệ (MainActivity)

#### A. Hình ảnh minh chứng

> **[HÌNH ẢNH 2.1: Giao diện danh sách liên hệ và khối thống kê tối giản]**  
> *(Dán ảnh chụp MainActivity với 3 liên hệ ban đầu: An, Bình, Châu và FAB ở góc dưới)*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện:**
  - **Thanh tiêu đề:** Tiêu đề lớn **"Danh bạ của tôi"** nằm bên trái, góc phải có biểu tượng avatar người dùng `ic_person_outline`.
  - **Khối thống kê tối giản:** Hiển thị dạng văn bản inline thanh lịch:
    - **3** `Liên hệ` (chữ số 3 in đậm màu đen).
    - **1** `Yêu thích` (chữ số 1 in đậm màu xanh teal).
  - **Danh sách liên hệ (`RecyclerView`):**
    - Mỗi item trong danh sách là thẻ `CardView` bo góc 16dp với viền cực mảnh tinh tế.
    - Ảnh đại diện tròn màu pastel nhẹ (xanh lá nhạt, tím nhạt,...) chứa chữ cái đầu viết hoa của tên (A, B, C) với màu chữ tương phản đậm.
    - Họ tên in đậm, số điện thoại màu xám và tên nhóm màu nhạt.
    - Biểu tượng ngôi sao xanh teal `☆` hiển thị ở góc phải đối với các liên hệ yêu thích (như Trần Thị Bình).
  - **Dòng nhắc:** "Nhấn vào một liên hệ để xem chi tiết" căn giữa ở đáy màn hình.
  - **Nút Floating Action Button (FAB):** Nút tròn màu xanh teal có biểu tượng dấu cộng `+` màu trắng đặt ở góc dưới cùng bên phải để mở màn hình Thêm liên hệ.
- **Tính toán khối thống kê:**
  - Tự động đếm và cập nhật số lượng liên hệ và số liên hệ yêu thích mỗi khi thêm, sửa hoặc xóa liên hệ.

---

### 2.3. Màn hình 3: Thêm liên hệ (AddContactActivity)

#### A. Hình ảnh minh chứng

> **[HÌNH ẢNH 3.1: Giao diện nhập thông tin liên hệ mới]**  
> *(Dán ảnh chụp màn hình AddContactActivity)*

> **[HÌNH ẢNH 3.2: Trường hợp báo lỗi validate khi nhập thiếu hoặc trùng số điện thoại]**  
> *(Dán ảnh chụp thông báo lỗi)*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện:**
  - Top Bar nền trắng: Mũi tên quay lại `←` bên trái, tiêu đề **"Thêm liên hệ"** ở giữa và nút văn bản **"Hủy"** ở bên phải (bấm để hủy bỏ thao tác và quay về).
  - Toàn bộ nội dung đặt trong `ScrollView`.
  - Các ô nhập `Họ và tên`, `Số điện thoại`, `Ghi chú` đều dùng nền xám nhẹ `#F3F4F6` bo góc mềm.
  - `Spinner` chọn Nhóm ("Bạn bè", "Gia đình", "Công việc", "Khác") có biểu tượng mũi tên chỉ xuống `˅`.
  - **Chọn Giới tính dạng Segmented Control 3 tab:** Khung nền xám chứa 3 nút tab `[ Nam | Nữ | Khác ]`. Khi chọn tab nào, tab đó sẽ sáng nền xanh ngọc nhạt `#D1FAE5` với chữ xanh đậm in đậm.
  - Checkbox Yêu thích dạng hàng ngang kèm biểu tượng ngôi sao xanh teal `☆`.
  - Nút "Lưu liên hệ" màu xanh teal bo tròn 14dp.
- **Quy tắc kiểm tra dữ liệu:**
  - Không cho phép họ tên rỗng.
  - Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng số `0` (Regex `^0[0-9]{9}$`).
  - Không cho phép số điện thoại trùng với bất kỳ liên hệ nào đã có trong danh bạ.

---

### 2.4. Màn hình 4: Chi tiết liên hệ (ContactDetailActivity)

#### A. Hình ảnh minh chứng

> **[HÌNH ẢNH 4.1: Giao diện chi tiết liên hệ]**  
> *(Dán ảnh chụp ContactDetailActivity)*

> **[HÌNH ẢNH 4.2: Thao tác gọi điện / nhắn tin qua ứng dụng hệ thống]**  
> *(Dán ảnh chụp màn hình quay số cuộc gọi)*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện:**
  - Top Bar với nút quay lại `←` và tiêu đề **"Chi tiết liên hệ"**.
  - **Khối trung tâm:**
    - Ảnh đại diện hình tròn lớn (80dp) màu pastel nhạt với chữ cái đầu viết hoa cỡ lớn.
    - Họ và tên lớn in đậm (ví dụ: **Trần Thị Bình**).
    - Số điện thoại hiển thị màu xám bên dưới.
    - Nút bấm gắn sao `☆ Yêu thích`: người dùng có thể chạm trực tiếp để chuyển đổi trạng thái yêu thích, lập tức cập nhật sang `MainActivity`.
  - **Hàng 3 nút hành động tròn:**
    - **Gọi điện:** Biểu tượng ống nghe xanh teal trong khung tròn pastel $\rightarrow$ gọi điện bằng `Intent.ACTION_DIAL`.
    - **Nhắn tin:** Biểu tượng tin nhắn $\rightarrow$ mở ứng dụng SMS bằng `Intent.ACTION_SENDTO` (`smsto:...`).
    - **Chỉnh sửa:** Biểu tượng cây bút chỉnh sửa.
  - **Các thẻ thông tin chi tiết:**
    - Hàng 2 thẻ bo góc độc lập: Thẻ **Nhóm** (có biểu tượng nhóm `ic_group_user`) và Thẻ **Giới tính**.
    - Thẻ **Ghi chú**: Có biểu tượng văn bản `ic_note_document` hiển thị nội dung ghi chú.
  - **Nút Xóa liên hệ:** Đặt ở dưới cùng gồm biểu tượng thùng rác đỏ `ic_trash_red` và dòng chữ đỏ in đậm "Xóa liên hệ". Khi bấm hiển thị hộp thoại xác nhận `AlertDialog` trước khi xóa.

---

## 3. CÁC ĐOẠN MÃ NGUỒN TIÊU BIỂU

### 3.1. Chuyển đổi tab Giới tính (Segmented Control)
```java
private void selectGender(String gender) {
    selectedGender = gender;
    // Đặt lại nền và màu sắc cho các tab
    tabGenderMale.setBackgroundResource(R.drawable.bg_gender_unselected);
    tabGenderFemale.setBackgroundResource(R.drawable.bg_gender_unselected);
    tabGenderOther.setBackgroundResource(R.drawable.bg_gender_unselected);
    
    // Nổi bật tab được chọn
    if ("Nam".equals(gender)) {
        tabGenderMale.setBackgroundResource(R.drawable.bg_gender_selected);
        tabGenderMale.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
    } else if ("Nữ".equals(gender)) {
        tabGenderFemale.setBackgroundResource(R.drawable.bg_gender_selected);
        tabGenderFemale.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
    } else {
        tabGenderOther.setBackgroundResource(R.drawable.bg_gender_selected);
        tabGenderOther.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
    }
}
```

### 3.2. Gọi điện và Nhắn tin bằng Intent ngầm định
```java
// 1. Gọi điện thoại
Intent dialIntent = new Intent(Intent.ACTION_DIAL);
dialIntent.setData(Uri.parse("tel:" + currentContact.getPhoneNumber()));
startActivity(dialIntent);

// 2. Gửi tin nhắn SMS
Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
smsIntent.setData(Uri.parse("smsto:" + currentContact.getPhoneNumber()));
startActivity(smsIntent);
```

---

## 4. KẾT LUẬN

Dự án đã đáp ứng hoàn hảo yêu cầu bài tập với thiết kế giao diện chuẩn xác theo phác thảo hiện đại:
- Bố cục thoáng đãng, màu sắc hài hòa, font chữ rõ ràng.
- Đầy đủ tính năng đăng nhập, xem danh sách, thống kê, thêm mới, xem chi tiết, gọi điện, nhắn tin, gắn sao yêu thích và xóa liên hệ.
- Code Java được cấu trúc rõ ràng, chuẩn theo các nguyên lý lập trình Android.
