## 📖 Документация API

### Swagger UI

После запуска приложения документация доступна по адресу:
- **Swagger UI:** http://localhost:8686/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8686/api-docs
- **OpenAPI YAML:** http://localhost:8686/api-docs.yaml


## 📊 База данных

### Структура таблиц
*Question schema*
```sql
create table questions (
	id SERIAL primary key,
	skill_id INT references skills(id) NOT NULL,
	title varchar not null,
	right_answer text,
	difficulty_level varchar(10) NOT NULL,
);

create table skills(
	id SERIAL primary key,
	title varchar(100) UNIQUE NOT NULL
);

create table positions(
	id SERIAL primary key,
	title varchar(100) not null unique
);

create table skill_requirements(
    id SERIAL primary key,
    position_id int references positions(id) on delete cascade not null,
    skill_id int references skills(id) not null,
    easy smallint not null check(easy >= 0 and easy <= 100),
    medium smallint not null check(medium >= 0 and medium <= 100),
    hard smallint not null check(hard >= 0 and hard <= 100),
    unique(position_id, skill_id)
)
```