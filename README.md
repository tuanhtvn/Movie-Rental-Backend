# Movie-Rental-Backend

1. Tài liệu API

- Swagger UI: <https://hoctuancustomservice.io.vn/swagger-ui/index.html>

2. Đặc tả API

- Google Sheets: <https://docs.google.com/spreadsheets/d/1DLbglDcHkxPFnhMuUJhEqARvxVsQB5Zv4YWFK9JopCs/edit?usp=sharing>

3. Cấu trúc thư mục trong dự án

```bash
        Movie-Rental-Backend/
        └── movie/
            └── src/
                └── main/
                    └── java/com/rental/movie
                        ├── common (Lưu trữ enum, interface, ...)
                        ├── component (Lưu trữ các bean khác không thuộc layer nào trong three-layer)
                        ├── config (Cấu hình hệ thống)
                        ├── controller (Nhận request và trả reponse)
                        ├── exception (Xử lý ngoại lệ)
                        ├── model (Lưu trữ và vận chuyển dữ liệu)
                        ├── repository (Xử lý logic truy vấn DB)
                        ├── service (Xử lý logic hệ thống)
                        └── util (Các xử lý khác)
```

- Luồng và data trong request: client gửi request -> (DTO)controller -> service -> (Entity)repository

- Luồng và data trong response: repository(Entity) -> service -> (DTO)controller -> client nhận response

4. Thông tin tài khoản - pass: 123456
- admin: rentalmovie68@gmail.com
- employee: employee@gmail.com
- user: user@gmail.com