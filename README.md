# TelegramTaskBot
A simple tracking bot 
### Building via Docker
1. Set *BOT_USERNAME* and *BOT_TOKEN* in **docker-compose.yml** 
2. Build and deploy bot with MySQL container using `docker-compose up`
__________
Возможны ситуации когда бот стартует раньше базы данных, в таких случаях приложение падает. Специально для этого в docker-compose.yml установлен параметр restart.
