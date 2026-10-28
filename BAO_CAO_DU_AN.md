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
Xây dựng ứng dụng Android hoàn chỉnh bằng **Java** và **XML layout** trên **Android Studio** nhằm quản lý danh bạ liên hệ cá nhân với giao diện trực quan, hiện đại, màu sắc chủ đạo xanh lục/teal. Ứng dụng gồm 4 màn hình:
1. **LoginActivity (Đăng nhập):** Xác thực tài khoản người dùng cố định (`admin` / `123456`).
2. **MainActivity (Danh sách liên hệ):** Hiển thị khối thống kê tổng số liên hệ, số liên hệ yêu thích và danh sách liên hệ qua `RecyclerView`.
3. **AddContactActivity (Thêm liên hệ):** Cho phép nhập thông tin liên hệ mới, kiểm tra hợp lệ dữ liệu (không để trống họ tên, số điện thoại 10 số bắt đầu bằng 0, không trùng số đã có).
4. **ContactDetailActivity (Chi tiết liên hệ):** Hiển thị thông tin chi tiết của liên hệ, cho phép gọi điện thoại qua Intent ngầm định (`ACTION_DIAL`), thay đổi trạng thái yêu thích, xóa liên hệ và quay lại danh sách.

### 1.2. Công nghệ và kỹ thuật áp dụng
- **Ngôn ngữ:** Java (JDK 8/17/25).
- **Giao diện:** XML Layout kết hợp Material Design Components (CardView, RecyclerView, Spinner, RadioGroup, CheckBox, Ripple effect, Shape Drawable).
- **Điều hướng & truyền dữ liệu:** Sử dụng `Intent` tường minh, `Intent` ngầm định (`ACTION_DIAL`), `ActivityResultLauncher` để nhận kết quả phản hồi (`setResult`).
- **Mô hình dữ liệu:** Lớp `Contact` implements `Serializable`.
- **Quản lý dữ liệu:** Lưu trữ trong bộ nhớ bằng `ArrayList<Contact>` qua lớp singleton `ContactManager`, có sẵn 3 liên hệ mẫu ban đầu đúng theo yêu cầu đề bài.

---

## 2. KIẾN TRÚC DỰ ÁN VÀ CẤU TRÚC THƯ MỤC

```
quanlydanhba/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/quanlydanhba/
│   │   │   ├── activity/
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── AddContactActivity.java
│   │   │   │   └── ContactDetailActivity.java
│   │   │   ├── adapter/
│   │   │   │   └── ContactAdapter.java
│   │   │   ├── data/
│   │   │   │   └── ContactManager.java
│   │   │   └── model/
│   │   │       └── Contact.java
│   │   └── res/
│   │       ├── drawable/ (các icon vector và background bo góc)
│   │       ├── layout/
│   │       │   ├── activity_login.xml
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_add_contact.xml
│   │       │   ├── activity_contact_detail.xml
│   │       │   ├── item_contact.xml
│   │       │   ├── spinner_item.xml
│   │       │   └── spinner_dropdown_item.xml
│   │       └── values/ (colors.xml, strings.xml, themes.xml)
├── build.gradle
├── settings.gradle
└── .gitignore
```

---

## 3. NỘI DUNG CHI TIẾT CÁC MÀN HÌNH

### 3.1. Màn hình 1: Đăng nhập (LoginActivity)

#### A. Hình ảnh minh chứng
*(Sinh viên chụp 2 ảnh: trường hợp đăng nhập báo lỗi và trường hợp đăng nhập bình thường)*

> **[HÌNH ẢNH 1.1: Giao diện màn hình đăng nhập khi vừa mở ứng dụng]**  
> *(Dán ảnh chụp màn hình LoginActivity tại đây)*

> **[HÌNH ẢNH 1.2: Giao diện khi nhập sai mật khẩu hoặc để trống thông tin]**  
> *(Dán ảnh chụp thông báo lỗi màu đỏ "Sai tên đăng nhập hoặc mật khẩu" hoặc "Vui lòng nhập đầy đủ...")*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện (UI Components):**
  - Khối biểu tượng điện thoại tròn nền xanh teal phía trên tạo điểm nhấn thương hiệu.
  - Tiêu đề `TextView` "Danh bạ" và phụ đề "Đăng nhập để tiếp tục".
  - `EditText` (`etUsername`): nhập tên đăng nhập với gợi ý "Nhập tên đăng nhập".
  - `EditText` (`etPassword`): nhập mật khẩu với `inputType="textPassword"` để ẩn ký tự dạng chấm tròn.
  - `TextView` (`tvError`): màu đỏ `#D32F2F`, mặc định ẩn (`gone`), hiển thị khi có lỗi xác thực.
  - `Button` (`btnLogin`): màu xanh teal bo tròn 25dp với hiệu ứng chạm (`ripple`).
  - `TextView` gợi ý tài khoản mẫu: `admin / 123456`.
- **Khai báo tài khoản cố định:**
  - Được khai báo là hằng số trực tiếp trong code Java:
    ```java
    private static final String FIXED_USERNAME = "admin";
    private static final String FIXED_PASSWORD = "123456";
    ```
- **Xử lý sự kiện và logic:**
  - Bắt sự kiện click vào nút Đăng nhập: lấy dữ liệu từ 2 ô nhập và loại bỏ khoảng trắng thừa (`trim()`).
  - Kiểm tra bỏ trống: nếu một trong hai ô rỗng, hiển thị thông báo lỗi và `Toast`.
  - Kiểm tra tính đúng đắn:
    - Nếu trùng khớp với tài khoản cố định: hiển thị `Toast` thành công, tạo `Intent` chuyển sang `MainActivity` và gọi `finish()` để giải phóng activity hiện tại, ngăn người dùng bấm phím Back quay lại màn hình đăng nhập:
      ```java
      Intent intent = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
      ```
    - Nếu không khớp: hiển thị thông báo lỗi "Sai tên đăng nhập hoặc mật khẩu" lên `tvError` và giữ nguyên màn hình.

---

### 3.2. Màn hình 2: Danh sách liên hệ (MainActivity)

#### A. Hình ảnh minh chứng

> **[HÌNH ẢNH 2.1: Giao diện danh sách liên hệ hiển thị dữ liệu mẫu và khối thống kê]**  
> *(Dán ảnh chụp MainActivity với 3 liên hệ ban đầu: Nguyễn Văn An, Trần Thị Bình, Lê Minh Châu)*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện (UI Components):**
  - **Thanh tiêu đề Top Bar:** Nền màu xanh teal `#00695C`, tiêu đề "Danh bạ của tôi" chữ trắng in đậm.
  - **Khối thống kê (CardView/LinearLayout bo góc 14dp):**
    - Chia làm 2 cột: Cột 1 hiển thị số lượng lớn và nhãn "Tổng liên hệ"; Cột 2 hiển thị số lượng lớn và nhãn "Yêu thích".
  - **Danh sách liên hệ (`RecyclerView`):**
    - Sử dụng `RecyclerView` kết hợp `LinearLayoutManager` và custom adapter `ContactAdapter`.
    - Mỗi item gồm:
      + Ảnh đại diện tròn (`FrameLayout` hình oval) chứa **chữ cái đầu của tên gọi** (ví dụ: An -> 'A', Bình -> 'B', Châu -> 'C') với màu nền phân biệt.
      + Họ và tên (`tvContactName`) in đậm rõ nét.
      + Số điện thoại (`tvContactPhone`).
      + Badge nhóm (`tvContactGroup`) bo góc với nền xanh ngọc nhạt `#E0F2F1`.
      + Biểu tượng ngôi sao vàng (`ivFavoriteStar`) hiển thị nếu liên hệ được đánh dấu yêu thích.
  - **Dòng gợi ý:** "(nhấn vào một liên hệ để xem chi tiết)".
  - **Nút Thêm liên hệ:** Nút bo tròn màu xanh teal cố định phía dưới màn hình, nhãn "+ Thêm liên hệ".
- **Dữ liệu mẫu ban đầu:**
  - Khởi tạo thông qua lớp `ContactManager` đúng theo đề bài:
    1. Nguyễn Văn An - 0901234567 - Bạn bè - Nam - Không yêu thích - Ghi chú rỗng.
    2. Trần Thị Bình - 0912345678 - Gia đình - Nữ - Yêu thích (Có) - Ghi chú: "Chị gái, hay gọi buổi tối".
    3. Lê Minh Châu - 0987654321 - Công việc - Khác - Không yêu thích - Ghi chú rỗng.
- **Tính toán khối thống kê:**
  - Hàm `updateStatistics()` duyệt danh sách `ArrayList<Contact>` để đếm tổng số liên hệ và số liên hệ có `isFavorite() == true`, sau đó gán lên `TextView`.
  - Được tự động gọi mỗi khi Activity khởi tạo, sau khi nhận kết quả từ màn hình thêm/xóa/sửa, và trong `onResume()`.
- **Xử lý sự kiện điều hướng:**
  - Nhấn nút Thêm: mở `AddContactActivity` qua `ActivityResultLauncher`.
  - Nhấn vào một liên hệ: gửi đối tượng `Contact` (qua `Serializable`) và vị trí `position` sang `ContactDetailActivity` bằng `Intent`.

---

### 3.3. Màn hình 3: Thêm liên hệ (AddContactActivity)

#### A. Hình ảnh minh chứng

> **[HÌNH ẢNH 3.1: Giao diện màn hình nhập liệu Thêm liên hệ]**  
> *(Dán ảnh chụp AddContactActivity với đầy đủ các trường nhập liệu)*

> **[HÌNH ẢNH 3.2: Báo lỗi khi để trống họ tên hoặc nhập số điện thoại sai quy cách / trùng lặp]**  
> *(Dán ảnh chụp thông báo lỗi validate)*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện (UI Components):**
  - Thanh tiêu đề Top Bar có nút mũi tên quay lại (`btnBackAdd`) và tiêu đề "Thêm liên hệ".
  - Bọc toàn bộ form trong `ScrollView` để tránh bị bàn phím che khuất.
  - 1. Họ và tên: `EditText` (`etName`), gợi ý "Ví dụ: Nguyễn Văn An".
  - 2. Số điện thoại: `EditText` (`etPhone`), `inputType="phone"`, giới hạn `maxLength="10"`.
  - 3. Nhóm: `Spinner` (`spGroup`) chứa 4 lựa chọn: "Gia đình", "Bạn bè", "Công việc", "Khác" với dropdown custom.
  - 4. Giới tính: `RadioGroup` gồm 3 `RadioButton`: "Nam", "Nữ", "Khác" (mặc định chọn Nam).
  - 5. Yêu thích: `CheckBox` (`cbFavorite`) "Thêm vào yêu thích".
  - 6. Ghi chú: `EditText` (`etNote`) nhiều dòng (`inputType="textMultiLine"`, `minLines="3"`).
  - 7. Nút Lưu liên hệ: `Button` (`btnSaveContact`) bo góc màu teal.
- **Quy tắc kiểm tra dữ liệu (Validation):**
  - **Họ và tên:** Kiểm tra `name.trim().isEmpty()` -> thông báo lỗi `setError` và `Toast` yêu cầu không được để trống.
  - **Số điện thoại:**
    - Kiểm tra bỏ trống: không được rỗng.
    - Kiểm tra cấu trúc hợp lệ bằng Biểu thức chính quy (Regex):  
      `phone.matches("^0[0-9]{9}$")`  
      Đảm bảo số điện thoại gồm đúng **10 chữ số** và **bắt đầu bằng chữ số 0**.
    - Kiểm tra trùng lặp trong danh bạ: gọi phương thức `ContactManager.getInstance().isPhoneExists(phone, -1)`. Nếu số đã tồn tại, hiển thị thông báo lỗi và ngăn chặn lưu.
- **Xử lý lưu dữ liệu và trả kết quả:**
  - Khởi tạo đối tượng `Contact` mới với đầy đủ thông tin vừa nhập.
  - Thêm đối tượng vào danh sách quản lý chung.
  - Đóng gói đối tượng `Contact` vào `Intent` trả về với `setResult(RESULT_OK, resultIntent)` rồi gọi `finish()` để quay về `MainActivity`.

---

### 3.4. Màn hình 4: Chi tiết liên hệ (ContactDetailActivity)

#### A. Hình ảnh minh chứng

> **[HÌNH ẢNH 4.1: Giao diện màn hình xem chi tiết một liên hệ]**  
> *(Dán ảnh chụp ContactDetailActivity hiển thị đầy đủ thông tin của liên hệ)*

> **[HÌNH ẢNH 4.2: Màn hình quay số cuộc gọi sau khi nhấn nút "Gọi điện"]**  
> *(Dán ảnh chụp ứng dụng Phone/Dialer mặc định của hệ thống mở ra với số điện thoại đã điền sẵn)*

> **[HÌNH ẢNH 4.3: Hộp thoại xác nhận khi nhấn nút "Xóa liên hệ"]**  
> *(Dán ảnh chụp AlertDialog xác nhận xóa)*

#### B. Mô tả chi tiết nội dung đã làm
- **Các thành phần giao diện (UI Components):**
  - Thanh tiêu đề Top Bar có nút mũi tên quay lại và tiêu đề "Chi tiết liên hệ".
  - Thẻ `CardView` lớn hiển thị:
    + Ảnh đại diện tròn chữ cái đầu lớn (64dp), tô màu theo ký tự.
    + Họ tên in đậm to và Số điện thoại màu xanh teal.
    + Nhóm và Giới tính chia làm 2 cột rõ ràng.
    + Khối văn bản hiển thị Ghi chú liên hệ.
    + `CheckBox` "Yêu thích".
  - 3 nút chức năng riêng biệt:
    + Nút 1: "Gọi điện" (màu teal, có biểu tượng ống nghe).
    + Nút 2: "Quay lại" (nền trắng viền xanh teal).
    + Nút 3: "Xóa liên hệ" (màu đỏ `#D32F2F`).
- **Xử lý nhận dữ liệu qua Intent:**
  - Nhận đối tượng `Contact` và chỉ số vị trí `position` trong `onCreate`:
    ```java
    currentContact = (Contact) getIntent().getSerializableExtra("contact");
    contactPosition = getIntent().getIntExtra("position", -1);
    ```
- **Xử lý cập nhật trạng thái yêu thích:**
  - Gắn listener `OnCheckedChangeListener` cho `CheckBox`: khi người dùng tích chọn hoặc bỏ tích, trạng thái `isFavorite` của đối tượng được cập nhật ngay lập tức vào bộ nhớ. Khi bấm Quay lại, kết quả cập nhật được gửi về để `MainActivity` làm mới danh sách và khối thống kê.
- **Xử lý nút Gọi điện (Intent ngầm định):**
  - Sử dụng `Intent.ACTION_DIAL` kèm `Uri` dạng `tel:...`:
    ```java
    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
    dialIntent.setData(Uri.parse("tel:" + currentContact.getPhoneNumber()));
    startActivity(dialIntent);
    ```
    Hành động này mở màn hình quay số mặc định của điện thoại với số điện thoại có sẵn, an toàn và không yêu cầu quyền nguy hiểm `CALL_PHONE`.
- **Xử lý nút Quay lại:**
  - Đóng gói đối tượng đã cập nhật vào `Intent`, gọi `setResult(RESULT_OK, resultIntent)` và `finish()`.
- **Xử lý nút Xóa liên hệ:**
  - Hiển thị hộp thoại `AlertDialog` hỏi người dùng: "Bạn có chắc chắn muốn xóa liên hệ này không?".
  - Nếu người dùng xác nhận "Xóa": xóa liên hệ khỏi `ContactManager`, trả cờ `is_deleted = true` về cho `MainActivity`, hiển thị `Toast` thông báo và đóng màn hình.

---

## 4. CÁC ĐOẠN MÃ NGUỒN TIÊU BIỂU

### 4.1. Lớp Model Contact (Thuật toán lấy chữ cái đầu của tên gọi tiếng Việt)
```java
public String getInitialLetter() {
    if (name == null || name.trim().isEmpty()) {
        return "?";
    }
    String trimmed = name.trim();
    String[] parts = trimmed.split("\\s+");
    String lastPart = parts[parts.length - 1]; // Lấy tên gọi (từ cuối cùng)
    if (!lastPart.isEmpty()) {
        return String.valueOf(lastPart.charAt(0)).toUpperCase();
    }
    return String.valueOf(trimmed.charAt(0)).toUpperCase();
}
```

### 4.2. Kiểm tra tính hợp lệ của số điện thoại (AddContactActivity)
```java
// Kiểm tra 10 chữ số và bắt đầu bằng số 0
if (!phone.matches("^0[0-9]{9}$")) {
    etPhone.setError("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
    etPhone.requestFocus();
    return;
}

// Kiểm tra trùng lặp
if (ContactManager.getInstance().isPhoneExists(phone, -1)) {
    etPhone.setError("Số điện thoại này đã tồn tại trong danh bạ!");
    etPhone.requestFocus();
    return;
}
```

### 4.3. Gọi điện bằng Intent ngầm định (ContactDetailActivity)
```java
Intent dialIntent = new Intent(Intent.ACTION_DIAL);
dialIntent.setData(Uri.parse("tel:" + currentContact.getPhoneNumber()));
startActivity(dialIntent);
```

---

## 5. KẾT LUẬN VÀ ĐÁNH GIÁ KẾT QUẢ

- Ứng dụng đã hoàn thành **100% tất cả các yêu cầu** trong đề bài:
  - Đúng chuẩn 4 màn hình theo sơ đồ chuyển màn hình và thiết kế phác thảo.
  - Sử dụng Java và XML thuần trên Android Studio.
  - Áp dụng đầy đủ Intent tường minh (chuyển màn hình, truyền đối tượng Contact) và Intent ngầm định (`ACTION_DIAL`).
  - Quản lý danh sách bằng `ArrayList<Contact>`, khởi tạo sẵn 3 dữ liệu mẫu ban đầu.
  - Khối thống kê tự động cập nhật chính xác theo thời gian thực khi thêm, sửa trạng thái yêu thích hoặc xóa liên hệ.
  - Kiểm tra tính hợp lệ dữ liệu chặt chẽ, không bị crash ứng dụng.
- Mã nguồn dự án được tổ chức khoa học, phân chia package rõ ràng (`activity`, `adapter`, `data`, `model`), có file `.gitignore` chuẩn cho Android Studio.
