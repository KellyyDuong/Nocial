from typing import List, Dict
from flask import Flask, jsonify, request
import mysql.connector

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
def calculateScore(screen_time_seconds):
    MAX_SCORE = 1000 # max possible points gained per day 
    MAX_SCREEN_TIME_SECONDS = 86400 # 24 hours in seconds

    screen_time_percent = min(screen_time_seconds / MAX_SCREEN_TIME_SECONDS, 1.0)
    score = int(round((1 - screen_time_percent) * MAX_SCORE))

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
def parseTotalTime(rawText):
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


@app.route('/getpfp/<userName>')
def getPfp(userName):
    return createImageFilePath(userName)


@app.route('/updateDailyScore/<userName>', methods=['GET', 'POST'])
def updateDailyScore(userName):
    connection = connect()
    cursor = connection.cursor()

    txt = request.form["userData"] # form sent from frontend should have name "userData"
    dailyScore = parseTotalTime(txt)
    dailyScore = calculateScore(dailyScore)

    cursor.execute(f"UPDATE users SET dailyScore = '{dailyScore}' WHERE userName = '{userName}'")
    connection.commit()

    cursor.close()
    connection.close()
    return str(dailyScore)
    

@app.route('/getGroupView/<groupID>')
def getGroupView(groupID):
    connection = connect()
    cursor = connection.cursor()

    cursor.execute(f"SELECT userName,dailyScore FROM users WHERE groupID1='{groupID}' OR groupID2='{groupID}' OR groupID3='{groupID}'") # list of tuples
    groupArr = [{'userName': userName, 'dailyScore': dailyScore} for (userName, dailyScore) in cursor]
    sortedGroupArr = sorted(groupArr, key=lambda x: x['dailyScore'], reverse=True) # sorted list of dicts

    cursor.execute(f"SELECT groupName, groupDesc FROM groups WHERE groupID='{groupID}'")
    gName = ""
    gDesc = ""
    for row in cursor: 
        gName = row[0]
        gDesc = row[1]

    returnObj = {}
    returnObj['groupName'] = gName
    returnObj['groupDesc'] = gDesc
    returnObj['groupMembers'] = sortedGroupArr

    # {
        # groupName: ""
        # groupDesc: ""
        # groupMembers: [
            # {
                # name: ''
                # score: ''
            # },
            # {
                # name: '' 
                # score: ''
            # }, 
        # ]
    # }

    cursor.close()
    connection.close()
    return returnObj


@app.route('/getHomeView/<userName>')
def getHomeView(userName):
    connection = connect()
    cursor = connection.cursor()

    cursor.execute(f"SELECT groupID1, groupID2, groupID3 FROM users WHERE userName='{userName}'")
    groupList = list(cursor.fetchone())
    # [2,1,3]

    groupData = []
    for id in groupList:
        cursor.execute(f"SELECT groupName, groupDesc FROM groups WHERE groupID={id}")
        groupDict = [{'groupName': groupName, 'groupDesc': groupDesc} for (groupName, groupDesc) in cursor]
        groupData.append(groupDict[0]) 

    # [
      # [{groupName, groupDesc}],
      # [{groupName, groupDesc}]
    # ]

    connection.close()
    cursor.close()
    return jsonify(groupData)
    

if __name__ == '__main__':
    app.run(host='0.0.0.0')
