# Calculator Application

A full-stack calculator application with a Vue.js frontend and Spring Boot backend.

## Project Structure

- `calculator-frontend/`: Vue.js frontend application
- `calculator-backend/`: Spring Boot backend application

## Setup Instructions

### Backend Setup

1. Navigate to the backend directory:
```sh
cd calculator-backend
```

2. Run the Spring Boot application:
```sh
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```sh
cd calculator-frontend
```

2. Install dependencies:
```sh
npm install
```

3. Run the development server:
```sh
npm run dev
```

The frontend will start on `http://localhost:5173`

## Development

- Frontend development server includes hot-reload
- Backend includes automatic restart on code changes
- Run tests:
  - Frontend: `cd calculator-frontend && npm run test:unit`
  - Backend: `cd calculator-backend && ./mvnw test`

## API Documentation

The calculator API provides the following endpoint:

- `POST /api/calculate`: Calculates mathematical expressions
  - Request body: `{ "expression": "2+2" }`
  - Response: `{ "expression": "2+2", "result": "4" }` 