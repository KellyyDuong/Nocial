from collections import OrderedDict
from flask import Flask
from flask import request
import mysql.connector
import json
import ast
import requests
from flask_cors import CORS


app = Flask(__name__)
app.config["JSON_SORT_KEYS"] = False
CORS(app)

config = {
    "user": "root",
    "password": "root",
    "host": "db",
    "port": "3306",
    "database": "politistock",
}

politicians_search_cols = [
    "state",
    "chamber",
    "preferred_name",
    "num_trades",
    "traded_volume",
    "party",
]
companies_search_cols = ["name", "foundedYear", "geo", "employees", "industry"]
stocks_search_cols = [
    "id",
    "displayName",
    "longName",
    "fullExchangeName",
    "marketCap",
    "fiftyTwoWeekHigh",
    "fiftyTwoWeekLow",
    "exchange",
]

dylan = "This is NOT a string :)"

table_to_search_cols = {
    "politicians": politicians_search_cols,
    "companies": companies_search_cols,
    "stocks": stocks_search_cols,
}


def get_all(table, search, sort, order, filters):
    connection = mysql.connector.connect(**config)
    cursor = connection.cursor(dictionary=True)
    query = f"SELECT * FROM {table}"

    # search
    if search != "":
        queries = str.split(search)
        search_cols = table_to_search_cols[table]

        # first search query
        query += f" WHERE ({search_cols[0]} LIKE '%{queries[0]}%'"
        for col in search_cols[1:]:
            query += f" OR {col} LIKE '%{queries[0]}%'"

        # all other search queries
        for s in queries[1:]:
            search_cols = table_to_search_cols[table]
            for col in search_cols:
                query += f" OR {col} LIKE '%{s}%'"
        query += ")"

    # filter
    if len(filters) > 0:
        query += generate_filter_query(filters, search)

    # sort
    if sort != "None":
        query += f" ORDER BY {sort} {order}"

    # execute query
    cursor.execute(query)

    result = OrderedDict()
    result["num_entries"] = 0  # add this first since to keep spot in OrderedDict
    for row in cursor:
        result[row["id"]] = row
    result["num_entries"] = len(result) - 1

    python_style_to_json(result, False)

    cursor.close()
    connection.close()

    return result


def get_all_paginated(table, limit, offset, search, sort, order, filters):
    connection = mysql.connector.connect(**config)
    cursor = connection.cursor(dictionary=True)
    query = f"SELECT * FROM {table}"

    # search
    if search != "":
        queries = str.split(search)
        search_cols = table_to_search_cols[table]

        # first search query
        query += f" WHERE ({search_cols[0]} LIKE '%{queries[0]}%'"
        for col in search_cols[1:]:
            query += f" OR {col} LIKE '%{queries[0]}%'"

        # all other search queries
        for s in queries[1:]:
            search_cols = table_to_search_cols[table]
            for col in search_cols:
                query += f" OR {col} LIKE '%{s}%'"
        query += ")"

    # filter
    if len(filters) > 0:
        query += generate_filter_query(filters, search)

    # sort
    if sort != "None":
        query += f" ORDER BY {sort} {order}"

    # execute query to get num_entries without pagination
    cursor.execute(query)
    temp_result = {}
    for row in cursor:
        temp_result[row["id"]] = row

    result = OrderedDict()
    result["num_entries"] = len(temp_result)

    # paginate
    query += f" LIMIT {limit} OFFSET {offset}"

    # execute paginated query
    cursor.execute(query)

    for row in cursor:
        result[row["id"]] = row

    python_style_to_json(result, False)

    cursor.close()
    connection.close()

    return result


def get_id(table, id):
    connection = mysql.connector.connect(**config)
    cursor = connection.cursor(dictionary=True, buffered=True)
    cursor.execute(f"SELECT * FROM {table} WHERE id = '{id}'")

    result = OrderedDict()
    for row in cursor:
        result[id] = row

    python_style_to_json(result, False)

    cursor.close()
    connection.close()

    return result

priya = "line 170"

priya = "testing branch merge right now"
def get_ids(table):
    connection = mysql.connector.connect(**config)
    cursor = connection.cursor(dictionary=True)
    cursor.execute(f"SELECT * FROM {table}")

    result = list()
    for row in cursor:
        result.append(row["id"])

    cursor.close()
    connection.close()

    return result


####################
# HELPER FUNCTIONS #
####################


def python_style_to_json(result, twitter=True):
    for key, val in result.items():
        if key != "num_entries" and key != "query":
            for (
                k,
                v,
            ) in val.items():
                cols_twitter = {
                    "companies",
                    "trades",
                    "tickers",
                    "category",
                    "geo",
                    "identifiers",
                    "metrics",
                    "parent",
                    "politicians",
                    "tags",
                    "twitter",
                    "ultimateParent",
                }
                cols = {
                    "companies",
                    "trades",
                    "tickers",
                    "category",
                    "geo",
                    "identifiers",
                    "metrics",
                    "parent",
                    "politicians",
                    "tags",
                    "ultimateParent",
                }

                use_cols = cols_twitter if twitter else cols
                if k in use_cols:
                    # ignore Unicode characters
                    strencode = v.encode("ascii", "ignore")
                    v = strencode.decode()
                    list_v = ast.literal_eval(v)
                    val[k] = list_v


def generate_filter_query(filters, search):
    filter_to_columns = {
        "state": "state_id",
        "party": "party",
        "role": "role",
        "industry": "industry_id",
        "country": "country_id",
        "chamber": "chamber",
        "exchange": "exchange",
        "trade_low": "num_trades",
        "tradevol_low": "traded_volume",
        "employee_low": "employees",
        "founding_low": "foundedYear",
        "52weekLow": "fiftyTwoWeekLow",
        "marketcap_low": "marketCap",
        "price_low": "ask",
        "trade_high": "num_trades",
        "tradevol_high": "traded_volume",
        "employee_high": "employees",
        "founding_high": "foundedYear",
        "52weekHigh": "fiftyTwoWeekHigh",
        "marketcap_high": "marketCap",
        "price_high": "ask",
    }
    low_limit_filters = {
        "52weekLow",
        "trade_low",
        "tradevol_low",
        "employee_low",
        "founding_low",
        "avgvol_low",
        "marketcap_low",
        "price_low",
    }
    high_limit_filters = {
        "52weekHigh",
        "trade_high",
        "tradevol_high",
        "employee_high",
        "founding_high",
        "avgvol_high",
        "marketcap_high",
        "price_high",
    }

    query_string = " WHERE (" if search == "" else " AND ("

    count = 0
    for f in filters:
        if f == "address":
            if count > 0:
                query_string += " AND "
            query_string += "("
            for idx, name in enumerate(filters[f]):
                if idx > 0:
                    query_string += " OR "
                query_string += f"preferred_name = '{name}'"
            query_string += ") "
        else:
            operator = "="
            quotes = "'"
            if count > 0:
                query_string += " AND "
            if f in low_limit_filters:
                operator = ">="
                quotes = ""
            elif f in high_limit_filters:
                operator = "<="
                quotes = ""
            query_string += (
                f"{filter_to_columns[f]} {operator} {quotes}{filters[f]}{quotes}"
            )
        count += 1

    if search != "None":
        query_string += ")"
    return query_string


def get_politician_filters():
    filter_list = [
        "address",
        "state",
        "party",
        "trade_low",
        "trade_high",
        "tradevol_low",
        "tradevol_high",
        "role",
        "chamber",
    ]
    filters = {}
    for f in filter_list:
        filter = request.args.get(f, "None")

        # get representative names by address
        if f == "address" and filter != "None":
            representative_names = []
            try:
                querystring = {
                    "address": filter,
                    "key": "AIzaSyBo_on7d2O_H2-2mVsNuFJbdMGyMswjYTE",
                }
                response = requests.request(
                    "GET",
                    "https://www.googleapis.com/civicinfo/v2/representatives",
                    params=querystring,
                )
                response = response.json()

                representative_indices = []

                if "offices" in response:
                    for office in response["offices"]:
                        if (
                            office["name"] == "U.S. Senator"
                            or office["name"] == "U.S. Representative"
                        ):
                            for idx in office["officialIndices"]:
                                representative_indices.append(idx)

                    for repr_idx in representative_indices:
                        representative_names.append(
                            response["officials"][repr_idx]["name"]
                        )

                    filters[f] = representative_names
                else:
                    filters[f] = ["dummy"]
            except Exception as e:
                print("google civic api request: something went wrong")

        elif filter != "None":
            filters[f] = filter
    return filters

"Stringgg"


def get_company_filters():
    filter_list = [
        "industry",
        "country",
        "employee_low",
        "employee_high",
        "founding_low",
        "founding_high",
    ]
    filters = {}
    for f in filter_list:
        filter = request.args.get(f, "None")
        if filter != "None":
            filters[f] = filter
    return filters


def get_stock_filters():
    filter_list = [
        "52weekHigh",
        "52weekLow",
        "marketcap_low",
        "marketcap_high",
        "price_low",
        "price_high",
        "exchange",
    ]
    filters = {}
    for f in filter_list:
        filter = request.args.get(f, "None")
        if filter != "None":
            filters[f] = filter
    return filters


#############
# ENDPOINTS #
#############


@app.route("/")
def index() -> str:
    result = dict()
    result["politicians_ids"] = get_ids("politicians")
    result["companies_ids"] = get_ids("companies")
    result["stocks_ids"] = get_ids("stocks")

    return json.dumps({"all_ids": result})


@app.route("/politicians")
def get_all_politicians():
    search = request.args.get("search", "")
    sort = request.args.get("sort", "None")
    order = request.args.get("order", "asc")
    filters = get_politician_filters()
    return get_all("politicians", search, sort, order, filters)


@app.route("/politician/<id>")
def get_politician(id):
    return get_id("politicians", id)


@app.route("/politicians/<limit>:<offset>")
def get_politicians_paginated(limit, offset):
    search = request.args.get("search", "")
    sort = request.args.get("sort", "None")
    order = request.args.get("order", "asc")
    filters = get_politician_filters()
    return get_all_paginated("politicians", limit, offset, search, sort, order, filters)


@app.route("/companies")
def get_all_companies():
    search = request.args.get("search", "")
    sort = request.args.get("sort", "None")
    order = request.args.get("order", "asc")
    filters = get_company_filters()
    return get_all("companies", search, sort, order, filters)


@app.route("/company/<id>")
def get_company(id):
    return get_id("companies", id)


@app.route("/companies/<limit>:<offset>")
def get_companies_paginated(limit, offset):
    search = request.args.get("search", "")
    sort = request.args.get("sort", "None")
    order = request.args.get("order", "asc")
    filters = get_company_filters()
    return get_all_paginated("companies", limit, offset, search, sort, order, filters)


@app.route("/stocks")
def get_all_stocks():
    search = request.args.get("search", "")
    sort = request.args.get("sort", "None")
    order = request.args.get("order", "asc")
    filters = get_stock_filters()
    return get_all("stocks", search, sort, order, filters)


@app.route("/stock/<id>")
def get_stock(id):
    return get_id("stocks", id)


@app.route("/stocks/<limit>:<offset>")
def get_stocks_paginated(limit, offset):
    search = request.args.get("search", "")
    sort = request.args.get("sort", "None")
    order = request.args.get("order", "asc")
    filters = get_stock_filters()
    return get_all_paginated("stocks", limit, offset, search, sort, order, filters)


if __name__ == "__main__":
    app.run(host="0.0.0.0")
