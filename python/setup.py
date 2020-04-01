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
            "pytest==5.4.1",
            "mypy==0.770",
            "flake8==3.7.9",
            "flake8-quotes==3.0.0"
        ],
    },
    include_package_data=True,
)
