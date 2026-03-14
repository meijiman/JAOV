# JAOV — Notes

Ghi chú kỹ thuật và giải thích cho các thành phần trong dự án.

---

## Thư viện (Libraries)

### libGDX 1.13.1

Framework game 2D/3D cho Java. Cung cấp mọi thứ cần thiết để chạy một game: vẽ hình ảnh lên màn hình (`SpriteBatch`, `ShapeRenderer`), đọc input chuột/bàn phím, load file ảnh/âm thanh/map, quản lý vòng lặp game (update → render lặp lại 60fps). Nếu không có libGDX, bạn phải tự viết toàn bộ phần này từ đầu.

### Ashley 1.7.4

Thư viện ECS (Entity Component System) — một kiến trúc giúp tổ chức code game theo cách dễ mở rộng. Thay vì dùng class kế thừa (`Hero extends Character extends Entity`), Ashley tách ra:

- **Entity**: chỉ là một cái ID, không có logic
- **Component**: data thuần túy (`PositionComponent`, `HealthComponent`)
- **System**: logic xử lý tất cả entity có component phù hợp (`MovementSystem` chỉ xử lý entity có `PositionComponent` + `MovementComponent`)

Kết quả: thêm tính năng mới = thêm component + system, không cần sửa code cũ.

### LWJGL3 (bundled theo libGDX)

Cầu nối giữa Java và hệ điều hành desktop (Windows/Linux/macOS). Tạo cửa sổ, kết nối OpenGL để vẽ đồ họa, nhận sự kiện chuột/bàn phím từ OS. Người dùng không tương tác trực tiếp với LWJGL3 — libGDX gọi nó ở bên dưới, bạn chỉ cần biết nó là backend chạy trên desktop. libGDX còn có backend khác cho Android, iOS, Web.
