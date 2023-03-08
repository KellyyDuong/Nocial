from typing import List, Dict
from flask import Flask
import mysql.connector
import json

import datetime
import time

app = Flask(__name__)

# connect
# inputs: none
# outputs: connection to MySQL database
def connect():
    config = {
        'user': 'root',
        'password': 'root',
        'host': 'db',
        'port': '3306',
        'database': 'mainDB'
    }
    connection = mysql.connector.connect(**config)
    return connection


# scores_byuser
# inputs: userName
# outputs: returns list with a map containing respective score for a given user
def scores_byuser(userName):
    connection = connect()
    cursor = connection.cursor()

    cursor.execute(f"SELECT userName, score FROM scores WHERE userName = '{userName}'")
    results = [{userName: score} for (userName, score) in cursor]
    cursor.close()
    connection.close()
    return results


# calculateScore
# inputs: userName
# outputs: convert time String in format H:M:S to seconds -> returns integer 
def calculateScore(userName) -> int:
    connection = connect()
    cursor = connection.cursor()
    cursor.execute(f"SELECT userName, dailyScreenTime FROM scores WHERE userName = '{userName}'")
    results = [{userName: screenTime} for (userName, screenTime) in cursor]

    score = time.strptime( results[0][userName].split(',')[0], '%H:%M:%S' )
    score = int(datetime.timedelta(hours=score.tm_hour, minutes=score.tm_min, seconds=score.tm_sec).total_seconds())

    cursor.close()
    connection.close()
    return score


# createImageFilePath
# inputs: userName
# outputs: generate String file path based on userName
# note: a more ideal solution would to integrate a cloud service like AWS and have images there
#       to download as necessary
def createImageFilePath(userName) -> str:
    connection = connect()
    cursor = connection.cursor()

    cursor.execute(f"SELECT userName, pfp FROM scores WHERE userName = '{userName}'")
    pfpMap = [{userName: pfpName} for (userName, pfpName) in cursor] 
    pfpPath = f"/assets/{pfpMap[0][userName]}.png".strip()

    cursor.close()
    connection.close()
    return pfpPath


@app.route('/getscore/<userName>')
def getScore(userName) -> str:
    return json.dumps(scores_byuser(userName))

@app.route('/updateScore/<userName>')
def updateScore(userName):
    connection = connect()
    cursor = connection.cursor()

    score = calculateScore(userName)
    cursor.execute(f"UPDATE scores SET score = '{score}' WHERE userName = '{userName}' ")
    connection.commit()
    cursor.execute(f"SELECT userName, score FROM scores WHERE userName = '{userName}'")
    results = [{userName: score} for (userName, score) in cursor]
    
    cursor.close()
    connection.close()
    return json.dumps(f'score: {score}, results: {results}')

@app.route('/getpfp/<userName>')
def getPfp(userName):
    connection = connect()
    cursor = connection.cursor()
    
    return createImageFilePath(userName)


if __name__ == '__main__':
    app.run(host='0.0.0.0')
