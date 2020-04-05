# Go implementation

## Status

:heavy_check_mark: Finished functionalities as per [README's requirements](../README.md#requirements)

## How to use?

    # run tests
    make test
    
    # install golangci-lint
    curl -sfL https://install.goreleaser.com/github.com/golangci/golangci-lint.sh | sh -s -- -b $(go env GOPATH)/bin v1.24.0
    
    # check for linting
    make lint
