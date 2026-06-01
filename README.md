# YouSpace

# YouSpace - Sistem Booking & Sewa Tempat Acara

## Deskripsi

YouSpace adalah aplikasi booking dan sewa venue yang membantu pengguna mencari dan memesan tempat untuk berbagai kegiatan seperti seminar, meeting, pernikahan, workshop, dan acara lainnya.

Aplikasi dikembangkan menggunakan JavaFX, SQLite, dan menerapkan konsep Object-Oriented Programming (OOP).

---

# Cara Menjalankan Aplikasi

## Prasyarat

* Java JDK 17 atau lebih baru
* Gradle
* SQLite JDBC Driver

## Menjalankan Aplikasi

Clone repository:

```bash
git clone <repository-url>
```

Masuk ke folder project:

```bash
cd YouSpace
```

Jalankan aplikasi:

```bash
gradle run
```

Saat aplikasi dijalankan pertama kali, file database SQLite akan dibuat secara otomatis:

```text
youspace.db
```

---

# Struktur Kode

```text
app
└── src
    └── main
        └── java
            └── youspace
                │
                ├── config
                │   └── Konfigurasi database SQLite
                │
                ├── database
                │   └── Inisialisasi dan pembuatan tabel database
                │
                ├── models
                │   └── Representasi objek aplikasi
                │      (AppUser, Customer, Admin, Venue, Booking, Payment)
                │
                ├── enums
                │   └── Konstanta status dan kategori
                │
                ├── dao
                │   └── Data Access Object untuk operasi database
                │
                ├── service
                │   └── Business logic aplikasi
                │
                ├── controllers
                │   └── Penghubung antara View dan Service
                │
                ├── utils
                │   └── Helper class dan utility
                │
                ├── view
                │   └── Tampilan JavaFX untuk User dan Admin
                

```


---

# Penerapan Pilar OOP

## 1. Encapsulation

Data pada class disimpan menggunakan atribut private dan diakses melalui getter dan setter.

Contoh:

```java
private String name;
private String email;
private String password;
private String phone;
```

Akses dilakukan melalui:

```java
getName()
setName()
getEmail()
setEmail()
```

Tujuan:

* Melindungi data dari akses langsung.
* Menambahkan validasi sebelum data diubah.

---

## 2. Inheritance

Class turunan mewarisi atribut dan method dari class induk.

Contoh:

```java
AppUser
│
├── Customer
└── Admin
```

Customer dan Admin mewarisi seluruh atribut dasar pengguna seperti:

```java
id
name
email
password
phone
role
status
```

dari class AppUser.

---

## 3. Polymorphism

Method yang sama dapat memberikan perilaku berbeda sesuai objek yang digunakan.

Contoh:

```java
public abstract String getDashboardName();
```

Pada class Customer:

```java
@Override
public String getDashboardName() {
    return "Dashboard User / Penyewa";
}
```

Pada class Admin:

```java
@Override
public String getDashboardName() {
    return "Dashboard Admin / Penyedia Venue";
}
```

Method yang sama menghasilkan output berbeda sesuai tipe objek.

---

## 4. Abstraction

Class abstrak digunakan untuk menyembunyikan detail implementasi dan hanya menampilkan fungsi penting.

Contoh:

```java
public abstract class AppUser
```

Class AppUser tidak dapat dibuat objek secara langsung, tetapi menjadi dasar bagi:

```java
Customer
Admin
```

Abstraction membantu menyederhanakan struktur program dan meningkatkan reusability kode.

---

# Database

Aplikasi menggunakan SQLite dengan tabel:

* users
* venues
* bookings
* payments
* favorites

Database dibuat otomatis saat aplikasi pertama kali dijalankan.

---


