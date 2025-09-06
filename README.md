## 📖 Документация API

### Swagger UI

После запуска приложения документация доступна по адресу:
- **Swagger UI:** http://localhost:8686/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8686/api-docs
- **OpenAPI YAML:** http://localhost:8686/api-docs.yaml


## 📊 База данных

### Структура таблиц
*Общее*
```sql
CREATE SCHEMA IF NOT EXISTS questions;
```

*Question schema*
```sql
create table questions.questions (
	id SERIAL primary key,
	skill_id INT references questions.skills(id) NOT NULL,
	title varchar not null,
	right_answer text,
	difficulty_level varchar(10) NOT NULL,
);

create table questions.skills(
	id SERIAL primary key,
	title varchar(100) UNIQUE NOT NULL
);
```