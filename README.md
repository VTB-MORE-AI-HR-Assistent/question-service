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

create table questions.positions(
	id SERIAL primary key,
	title varchar(100) not null unique
);

create table questions.skill_requirements(
    id SERIAL primary key,
    position_id int references questions.positions(id) on delete cascade not null,
    skill_id int references questions.skills(id) not null,
    easy smallint not null check(easy >= 0 and easy <= 100),
    medium smallint not null check(medium >= 0 and medium <= 100),
    hard smallint not null check(hard >= 0 and hard <= 100),
    unique(position_id, skill_id)
)
```