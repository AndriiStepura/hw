Restful API service CRD with GitHub gist for hw3 (Section 3 - API Testing )

## Getting Started

0. Open env.json and set your credentials at 7th and 16th line wit your GitHub user and password

1. For Run you can import collection and env to Postman and run with Postman Runner
or run with CLI Newman (https://www.npmjs.com/package/newman):
- install Node.js
- install npm
- install Newman with "npm install -g newman"
- Run tests in folder \hw\3\ with command "newman run collection.json -e env.json -r cli,junit --color on"