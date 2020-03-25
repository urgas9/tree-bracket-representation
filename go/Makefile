lint:
	@echo "linting ..."
	golangci-lint run ./...

test:
	@echo "running tests ..."
	@go test -race -count=1 -coverprofile=test-coverage.out ./...
	@echo "done"

visualise-coverage:
	@go tool cover -html=test-coverage.out
