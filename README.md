<p align="center">
  <img src="public/images/ecalc-banner.png" alt="E-Concalc Banner" width="100%">
</p>

# E-Concalc - Electronic Calculator

E-Concalc is a modern, powerful, and sleek calculator web application built with **Laravel**. It provides a premium user experience with advanced calculation features and a beautiful interface.

## 🚀 Features

-   **Standard & Scientific Calculations**: Perform basic arithmetic to complex scientific functions.
-   **Unit Converter**: Easily convert between different units (Length, Weight, Temperature, etc.).
-   **History Log**: Keep track of your past calculations.
-   **Responsive Design**: optimized for desktop, tablet, and mobile devices.
-   **Dark Mode**: A visually stunning dark theme for comfortable usage.

## � Screenshots

| Login Page | Calculator |
|:---:|:---:|
| <img src="public/images/screenshot-login.png" width="400" alt="Login Page"> | <img src="public/images/screenshot-calculator.png" width="400" alt="Calculator Page"> |

| Unit Converter | Financial Calculator |
|:---:|:---:|
| <img src="public/images/screenshot-converter.png" width="400" alt="Converter Page"> | <img src="public/images/screenshot-financial.png" width="400" alt="Financial Page"> |

| Currency Converter | Health Calculator |
|:---:|:---:|
| <img src="public/images/screenshot-currency.png" width="400" alt="Currency Page"> | <img src="public/images/screenshot-health.png" width="400" alt="Health Page"> |

## �🛠️ Tech Stack

-   **Backend**: Laravel (PHP)
-   **Frontend**: Blade Templates, Vanilla CSS / JavaScript
-   **Database**: MySQL (for saving history/user data)

## 📦 Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/falcon-ioi/Caclcon.git
    cd Caclcon
    ```

2.  **Install Dependencies**
    ```bash
    composer install
    npm install
    ```

3.  **Environment Setup**
    Copy the example env file and configure your database credentials:
    ```bash
    cp .env.example .env
    php artisan key:generate
    ```

4.  **Run Migrations**
    ```bash
    php artisan migrate
    ```

5.  **Start the Server**
    ```bash
    php artisan serve
    ```
    Visit `http://localhost:8000` in your browser.

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## 📄 License

This project is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).
