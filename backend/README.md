# Celfocus Hiring Playground | Backend
# <div align= "center"><img src="../images/banner-backend.png" alt="Repo Banner"></div>

This is the backend for the Celfocus Playground Kickstarter. Here you can find the overview of the structure, and how to get started. And here you'll operate!

The backend is a typical [Spring Boot](https://spring.io/projects/spring-boot) application that exposes a REST API for a shopping cart.

## Project Structure

```
project/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── pom.xml
│   └── docker-compose.yml
│
├── frontend/
├── mocks/
└── docker-compose.yml
```

## Key Features

### 1. REST API

**_Shopping Cart API_**: The backend exposes a REST API for managing the shopping cart, including adding, removing, and viewing items in the cart.  

### 2. Service Layer 

`CartService`: Contains the business logic for managing the shopping cart. The `CartService` class provides methods to:
- Add items to the cart
- Remove items from the cart
- Clear the cart
- Retrieve the cart for a specific user

`ProductService`: Contains the business logic for managing products. The `ProductService` class provides methods to:
- Retrieve all products
- Retrieve a product by SKU

### 3. Repository Layer  

Manages the persistence of data. 

> [!TIP]
> The products are populated with a pre-made list from a JSON file during the app initialization using `ProductsLoader`, please refer to the [Products List](../backend/src/main/resources/products.json).

## Running the Project

To run the project locally, follow these steps: 

1.  **Clone the Project repository to your local machine.**

2. **Build the Project**: Ensure all necessary dependencies are installed and the project is built:  

    ```bash
    cd backend
    mvn clean install
    ```

3. **Start the Backend**: To start the backend Spring Boot application:  

    ```bash
    mvn spring-boot:run
    ```
   
The application should now be running at http://localhost:8080.