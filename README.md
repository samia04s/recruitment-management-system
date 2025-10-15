# Recruitment Management System

A backend server for managing recruitment, job postings, and applicant profiles, built with Spring Boot and MySQL.

---

## Features

- User Registration & Login (JWT authentication)
- Applicant Profile Creation
- Resume Upload (PDF/DOCX only)
- Resume Parsing via third-party API (Apilayer)
- Admin Job Management (create/view jobs)
- Applicant Job Application
- Admin View of Applicants and Extracted Resume Data
- Environment Variable Support for sensitive config

---

## Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA
- MySQL
- JWT (JSON Web Token)
- Apilayer Resume Parser API
- Maven

---

## Setup Instructions

### 1. Clone the Repository
git clone https://github.com/samia04s/recruitment-management-system.git
cd recruitment-management-system



---

## API Endpoints

| Method | Endpoint                                      | Description                        | Auth Required |
|--------|-----------------------------------------------|------------------------------------|--------------|
| POST   | `/api/auth/signup`                            | User registration                  | No           |
| POST   | `/api/auth/login`                             | User login (returns JWT)           | No           |
| POST   | `/api/applicant/uploadResume`                 | Upload resume (PDF/DOCX)           | Yes (Applicant)|
| POST   | `/api/admin/job`                              | Create job opening                 | Yes (Admin)  |
| GET    | `/api/jobs`                                   | List job openings                  | Yes          |
| POST   | `/api/jobs/apply?job_id={job_id}`             | Apply to job                       | Yes (Applicant)|
| GET    | `/api/admin/job/{job_id}`                     | View job details & applicants      | Yes (Admin)  |
| GET    | `/api/admin/applicants`                       | List all users                     | Yes (Admin)  |
| GET    | `/api/admin/applicant/{applicant_id}`         | View applicant resume data         | Yes (Admin)  |

---

## Testing

- Use [Postman](https://www.postman.com/) to test all endpoints.
- Authenticate using JWT tokens for protected endpoints.

---

## Contribution

Pull requests and issues are welcome!

---

## License

MIT

---


