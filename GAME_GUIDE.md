# JAOV — Hướng Dẫn Chơi Game

---

## JAOV là gì?

JAOV là nguyên mẫu game MOBA (Multiplayer Online Battle Arena — Đấu trường trực tuyến nhiều người chơi) góc nhìn từ trên xuống dạng 2D. Hai đội — **Xanh (Blue)** và **Đỏ (Red)** — đối đầu nhau trên chiến trường ba đường. Mỗi đội vừa bảo vệ trụ sở của mình, vừa tiến công để phá hủy trụ sở đối thủ.

---

## Bắt Đầu

Khởi động game bằng lệnh:

```bash
./gradlew :app:run
```

Game mở ở cửa sổ **1280×720**. Tướng (Hero) của bạn xuất hiện tại **trụ sở Xanh (Blue Base)** ở góc dưới bên trái bản đồ.

---

## Điều Khiển (Controls)

| Thao tác | Hành động |
|---|---|
| **Chuột phải (Right-click)** lên bản đồ | Di chuyển tướng đến vị trí đó |

---

## Bản Đồ (Map)

Chiến trường là bản đồ hình vuông lớn chia thành ba đường (Lane) nối hai trụ sở (Base).

```
┌─────────────────────────────────┐
│  Trụ sở Đỏ (Red Base)           │
│                  ↑ Đường trên   │
│    Sông ╲       ╱  (Top Lane)   │
│  (River)  ╲   ╱                 │
│            ╲ ╱  Đường giữa      │
│            ╱ ╲  (Mid Lane)      │
│           ╱   ╲  Sông (River)   │
│  Đường dưới ↓   ╲               │
│  (Bot Lane)                     │
│  Trụ sở Xanh (Blue Base)        │
└─────────────────────────────────┘
```

| Khu vực | Mô tả |
|---|---|
| **Trụ sở Xanh (Blue Base)** | Góc dưới trái. Điểm xuất phát của bạn. |
| **Trụ sở Đỏ (Red Base)** | Góc trên phải. Căn cứ của kẻ địch. |
| **Đường trên (Top Lane)** | Chạy dọc cạnh trái rồi ngang phía trên. |
| **Đường giữa (Mid Lane)** | Chạy chéo thẳng từ trụ sở Xanh đến trụ sở Đỏ — con đường ngắn nhất. |
| **Đường dưới (Bot Lane)** | Chạy ngang phía dưới rồi dọc cạnh phải. |
| **Sông (River)** | Chạy chéo vuông góc với đường giữa, chia đôi bản đồ. |

---

## Trụ (Tower)

Mỗi đội có **9 trụ (Tower)** — 3 trụ mỗi đường, sắp xếp từ xa đến gần trụ sở:

| Hạng | Vị trí | Kích thước |
|---|---|---|
| **Trụ ngoài (Outer)** | Xa trụ sở nhất, tuyến phòng thủ đầu tiên | Nhỏ nhất |
| **Trụ giữa (Mid)** | Giữa đường | Vừa |
| **Trụ trong (Inner)** | Gần trụ sở nhất, tuyến phòng thủ cuối cùng | Lớn nhất |

Trụ đội Xanh (Blue) có màu xanh, trụ đội Đỏ (Red) có màu đỏ.

---

## Bản Đồ Thu Nhỏ (Minimap)

**Bản đồ thu nhỏ (Minimap)** kích thước 200×200px luôn hiển thị ở **góc trên bên trái** màn hình.

| Ký hiệu | Ý nghĩa |
|---|---|
| Vòng tròn trắng | Tướng (Hero) của bạn |
| Ô vuông xanh | Trụ (Tower) đội Xanh (Blue) |
| Ô vuông đỏ | Trụ (Tower) đội Đỏ (Red) |
| Vòng tròn xanh lá | Lính (Minion) hoặc đơn vị khác |
| Dải màu nâu | Đường (Lane) |
| Dải màu xanh dương | Sông (River) |

---

## Mẹo Chơi

- Bản đồ rất rộng — dùng bản đồ thu nhỏ (Minimap) để định hướng và lên kế hoạch di chuyển.
- Đường giữa (Mid Lane) là con đường ngắn nhất giữa hai trụ sở (Base).
- Chuột phải (Right-click) xa về phía trước để tướng (Hero) di chuyển hiệu quả hơn.
