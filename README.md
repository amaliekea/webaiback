# StudyHelper – Backend

**StudyHelper** is the backend part of a project designed to help students better understand academic topics. It uses external APIs to generate explanations, quiz questions, and related articles.

---

## Features

- Generates explanations using the **OpenAI API**
- Adds quiz questions from **QuizAPI.io** (optional)
- Includes a related article from **NewsAPI**

---

## How It Works

1. The user sends a topic, a difficulty level (`low`, `medium`, or `high`), and whether a quiz should be included.
2. The backend responds with:
   - A generated explanation
   - Quiz questions (if requested)
   - A short article summary

---

## Example Request (POST `/study-helper`)

```json
{
  "topic": "Java Streams",
  "level": "medium",
  "includeQuiz": true
}
## 🔐 Configuration

Add your API keys in the `application.properties` file:

```properties
OPENAPIKEY=your_openai_api_key
QUIZAPIKEY=your_quizapi_key
ARTICLEAPIKEY=your_newsapi_key

## 🔨 Technologies Used

- Java 17  
- Spring Boot  
- WebClient (for async HTTP requests)  
- OpenAI API  
- QuizAPI.io  
- NewsAPI.org  

---

## 👩‍💻 Developed By

**https://github.com/AndersNystrupJ**
**https://github.com/AndersNystrupJ**  
**https://github.com/zuunun**  
**https://github.com/Aluna0001**  

3rd Semester Computer Science Students (Datamatiker)  
_This backend was created as part of an academic assignment.
