from typing import List, Dict
from flask import Flask, jsonify, request
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


# calculateScore
# inputs: userName
# outputs: convert time String in format H:M:S to minutes -> returns integer 
def calculateScore(userName) -> int:
    connection = connect()
    cursor = connection.cursor()
    cursor.execute(f"SELECT userName, dailyScreenTime FROM users WHERE userName = '{userName}'")
    results = [{userName: screenTime} for (userName, screenTime) in cursor]

    score = time.strptime( results[0][userName].split(',')[0], '%H:%M:%S' )
    score = int(datetime.timedelta(hours=score.tm_hour, minutes=score.tm_min, seconds=score.tm_sec).total_seconds() / 60)

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

    cursor.execute(f"SELECT userName, pfp FROM users WHERE userName = '{userName}'")
    pfpMap = [{userName: pfpName} for (userName, pfpName) in cursor] 
    pfpPath = f"/assets/{pfpMap[0][userName]}.png".strip()

    cursor.close()
    connection.close()
    return pfpPath


# parseTotalScore
# inputs: text from Java app containing each app and its usage in seconds
# outputs: an integer sum of all the seconds
def parseTotalScore(rawText) -> int:
    times = []

    for line in rawText.split('\n'):
        line = line.strip()
        if line:
            number_str = line.split(':')[-1].strip().split()[0]
            times.append(int(number_str))

    return sum(times)



@app.route('/')
def main():
    return "Nocial"


@app.route('/<userName>')
def profile(userName):
    connection = connect()
    cursor = connection.cursor()

    cursor.execute(f"SELECT * FROM users WHERE userName = '{userName}'")
    userInfo = list(cursor.fetchone())

    cursor.close()
    connection.close()
    return jsonify(userInfo) # returns list contents as JSON response -> list of Strings accessible


@app.route('/updateScore/<userName>')
def updateScore(userName):
    connection = connect()
    cursor = connection.cursor()

    score = calculateScore(userName)
    cursor.execute(f"UPDATE users SET dailyScore = '{score}' WHERE userName = '{userName}' ")
    connection.commit()
    cursor.execute(f"SELECT userName, dailyScore FROM users WHERE userName = '{userName}'")
    results = [{userName: score} for (userName, score) in cursor]
    
    cursor.close()
    connection.close()
    return results[0][userName]


@app.route('/getpfp/<userName>')
def getPfp(userName):
    return createImageFilePath(userName)


@app.route('/updateTotalScore/<userName>', methods=['GET', 'POST'])
def debug(userName):
    connection = connect()
    cursor = connection.cursor()

    # POST request
    txt = request.form["userData"]
    totalScore = parseTotalScore(txt)
    
    cursor.execute(f"UPDATE users SET totalScore = '{totalScore}' WHERE userName = '{userName}'")
    connection.commit()
    
    cursor.close()
    connection.close()
    # sends it to the frontend
    return totalScore


if __name__ == '__main__':
    app.run(host='0.0.0.0')
