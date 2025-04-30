# Tontine Project Structure Analysis

## Overview
The Tontine project is a full-stack web application for managing tontine groups (Hui). It is built using the JHipster framework, which combines Spring Boot for the backend and Angular for the frontend.

## Project Structure

### Backend (Java)
The backend follows a standard Spring Boot application structure:

- **Domain Layer**: Contains the core business entities
  - `Hui`: Represents a tontine group
  - `HuiVien`: Represents a member of a tontine group
  - `ChiTietHui`: Represents the details of a member's participation in a tontine group
  - `HotHui`: Represents an active tontine group (not directly mapped to a database table)
  - `LoaiHui`: Enum for different types of tontine groups (daily, weekly, etc.)

- **Repository Layer**: Data access interfaces for the domain entities
  - Uses Spring Data JPA for database operations

- **Service Layer**: Business logic implementation
  - Contains services for managing tontine groups, members, and their interactions

- **Web Layer**: REST API endpoints
  - Contains resources (controllers) for exposing the business functionality as REST APIs

- **Response DTOs**: Data Transfer Objects for API responses
  - `DayHuiResponse`: Response DTO for tontine group information
  - `HuiKhuiResponse`: Response DTO for tontine group information

- **Mapper Layer**: Object mappers for transforming between entities and DTOs
  - `HuiKhuiMapper`: Maps between Hui entities and DTOs

### Frontend (Angular)
The frontend follows a standard Angular application structure:

- **Modules**: Feature modules for different parts of the application
  - `account`: User account management
  - `admin`: Administration functionality
  - `chi-tiet-hui`: Tontine details management
  - `ds-hui-khui`: Tontine list/management
  - `hui`: Tontine management
  - `hui-vien`: Tontine member management
  - `login`: Authentication

- **Components**: UI components for the different modules
  - Each module contains components for listing, creating, updating, and deleting entities

- **Services**: Angular services for communicating with the backend APIs

### Database
The database schema is managed using Liquibase and includes:

- `hui`: Table for storing tontine groups
- `hui_vien`: Table for storing tontine members
- `chi_tiet_hui`: Table for storing the details of a member's participation in a tontine group

## Business Model
The application manages tontine groups (Hui) with the following key concepts:

1. **Hui (Tontine Group)**:
   - A group where members contribute money regularly
   - Has properties like name, creation date, type, amount, etc.
   - Can be of different types: daily, weekly, every ten days, bi-weekly, monthly

2. **HuiVien (Tontine Member)**:
   - A person who participates in one or more tontine groups
   - Has properties like name, phone number, etc.

3. **ChiTietHui (Tontine Detail)**:
   - Represents the relationship between a tontine group and a member
   - Tracks details like bidding amount, date, cycle, hot money, etc.

4. **HotHui (Active Tontine)**:
   - Represents an active tontine group
   - Tracks details like amounts paid out, remaining amounts, etc.

## Technology Stack
- **Backend**: Java, Spring Boot, Spring Data JPA, Spring Security
- **Frontend**: Angular, TypeScript, HTML, SCSS
- **Database**: Supports H2 (development) and PostgreSQL (production)
- **Build Tools**: Maven (backend), npm (frontend)

## Conclusion
The Tontine project is a well-structured full-stack application that follows modern development practices and architecture patterns. It uses the JHipster framework to combine Spring Boot and Angular, providing a robust foundation for building and maintaining the application.
