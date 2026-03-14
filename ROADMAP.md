# JAOV — Roadmap

Các tính năng (Feature) dự kiến, sắp xếp theo thứ tự ưu tiên.

---

## Nhóm 1 — Gameplay cốt lõi (Core Mechanics)

- [ ] **Health system** — HP cho tướng (Hero), trụ (Tower), lính (Minion) → `HealthComponent`, `HealthBarSystem`
- [ ] **Tower attack** — Trụ tự động tấn công kẻ địch vào trong tầm → `AttackComponent`, `TowerAISystem`
- [ ] **Hero attack** — Click vào trụ / kẻ địch để tấn công → `CombatSystem`
- [ ] **Tower destruction** — Trụ bị phá hủy khi HP = 0, xóa khỏi engine → xử lý trong `CombatSystem`

---

## Nhóm 2 — Lính (Minion)

- [ ] **Minion spawn** — Lính tự spawn định kỳ từ base, đi theo đường (Lane) → `MinionSpawnSystem`
- [ ] **Minion pathfinding** — Đi theo waypoint dọc lane, không cần A* → `WaypointComponent` + cập nhật `MovementSystem`
- [ ] **Minion AI** — Tự tấn công trụ / tướng địch gần nhất → `MinionAISystem`

---

## Nhóm 3 — UI / HUD

- [ ] **HP bar** — Thanh máu hiển thị trên đầu mỗi entity → `HealthBarSystem` dùng `ShapeRenderer`
- [ ] **Hero HUD** — Thanh HP/Mana cố định góc dưới màn hình → render screen-space như `MiniMap`
- [ ] **Kill/Death counter** — Đếm số trụ đã phá / số lần chết → `GameStatsComponent`

---

## Nhóm 4 — Kỹ thuật (Technical)

- [ ] **Camera clamp** — Camera không scroll ra ngoài biên bản đồ → 2 dòng trong `GameScreen.render()`
- [ ] **Collision với trụ** — Tướng không đi xuyên qua trụ → `CollisionSystem` hoặc check trong `MovementSystem`
- [ ] **Fog of war** — Ẩn khu vực chưa khám phá → `VisibilitySystem` + render mask

---

## Nhóm 5 — Nâng cao (Advanced)

- [ ] **Skill / Ability system** — Kỹ năng tướng với cooldown
- [ ] **Gold & item shop** — Farm gold từ lính, mua đồ
- [ ] **Level up** — Tướng tăng level, chỉ số (stat) tăng theo
- [ ] **Âm thanh (Sound)** — Nhạc nền, hiệu ứng tấn công / chết

---
