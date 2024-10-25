et -e

# Очищуємо та збираємо проект
./gradlew clean build

# Будуємо Docker образ
docker build -t dmytroafro/latest2 .

# Завантажуємо Docker образ на Docker Hub
docker push dmytroafro/latest2

