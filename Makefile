build:
	mvn compile

unit-test:
	mvn test

integration-test:
	mvn test -P integration-test

test: unit-test integration-test

performance-test: docker-start
	mvn gatling:test -P performance-test
	make docker-down

package:
	mvn package

docker-rmi:
	@echo Apagando a imagem background:latest
	@docker rmi -f background:latest || true

docker-build: package docker-rmi
	docker build --build-arg PROFILE=$(PROFILE) -t background:latest -f ./Dockerfile .

docker-run: docker-stop
	@docker rm -f rr-api || true
	docker run --env-file .env --name rr-api -p 8080:8080 -d background:latest

docker-stop:
	@docker stop rr-api || true

docker-start: docker-down docker-rmi
	@echo Subindo o conainer Docker
	docker compose up --build -d

docker-down:
	@echo Removendo container antigo...
	docker-compose down || true

