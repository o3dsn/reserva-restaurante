build:
	mvn compile

unit-test:
	mvn test

integration-test:
	mvn test -P integration-test

test: unit-test integration-test

performance-test:
	mvn gatling:test -P performance-test

package:
	mvn package

docker-build: package
	docker build --build-arg SPRING_PROFILE=$(PROFILE) -t background:latest -f ./Dockerfile .

docker-start:
	docker-compose up --build -d

docker-down:
	docker-compose down

