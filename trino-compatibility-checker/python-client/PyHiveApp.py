import importlib


class PythonApp:
    def __init__(self):
        self.url = ""


if __name__ == '__main__':
    HOST = 'host'
    PORT = 8080
    SCHEMA = 'schema'
    TABLES = ["table"]

    print("Application starting...")
    
    from pyhive import trino

    conn = trino.connect(
        host=HOST,
        port=PORT,
        catalog='hive',
        schema=SCHEMA,
    )

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

    cur.close()
    conn.close()

    print("Application completed.")
