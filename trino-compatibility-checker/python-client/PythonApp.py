import importlib


class PythonApp:
    def __init__(self):
        self.url = ""


if __name__ == '__main__':
    DRIVER = "trino"
    HOST = 'host'
    PORT = 8080
    SCHEMA = 'schema'
    TABLES = ["table"]

    print("Application starting...")

    db = importlib.import_module(DRIVER)
    print("Connected using " + DRIVER + " " + db.__version__)
    conn = db.dbapi.connect(
        host=HOST,
        port=PORT,
        user='python-check',
        catalog='hive',
        schema=SCHEMA,
    )
    with conn:
        cur = conn.cursor()
        for tbl in TABLES:
            query = "SELECT * FROM " + tbl + " LIMIT 1"
            print("Querying " + query)
            cur.execute(query)
            rows = cur.fetchall()
            print("Columns: ")
            print(cur.description)
            for row in rows:
                print("-------------ROW--------------")
                print(row)

    print("Application completed.")
