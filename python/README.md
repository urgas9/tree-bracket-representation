# Python implementation

## Status

:heavy_check_mark: Finished functionalities as per [README's requirements](../README.md#requirements)

## How to use?

    # create virtual environment
    virtualenv -p python3 venv
    source venv/bin/activate
    
    # install all dependencies, including test
    pip install .[tests]
    
    # run tests
    make test
    
    # check linting with flake8
    make lint
    
    # check type hints with mypy
    make type-hint
