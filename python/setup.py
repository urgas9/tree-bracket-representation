from setuptools import find_packages, setup

setup(
    name="bracket-tree",
    author="urgas9",
    version="0.0.1",
    long_description="Bracket Tree parser",
    python_requires=">=3.5",
    packages=find_packages(
        include=[
            "brackettree",
        ],
        exclude=[
            "tests",
        ],
    ),
    install_requires=[
    ],
    extras_require={
        "tests": [
            "pytest",
            "mypy",
            "flake8",
            "flake8-quotes"
        ],
    },
    include_package_data=True,
)
