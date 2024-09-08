import random
import pymysql


host = 'localhost'
user = 'root'
password = '123456'
database = 'huiyu'

start = 100000
end = 999999
table_name = 'user_id_sender'


def generate_random_numbers(start, end):
    numbers = random.sample(range(start, end + 1), end - start + 1)
    return numbers


def insert_numbers(table_name, numbers):
    # 打乱插入900000个ID

    conn = pymysql.connect(host=host, user=user, password=password, database=database)
    cursor = conn.cursor()

    count = 0
    # 每次取出numbers的1000条数据，批量插入到数据库中
    for i in range(0, len(numbers), 1000):
        sql = f"INSERT INTO {table_name} (user_id) VALUES "
        for number in numbers[i: i + 1000]:
            sql += f"({number}),"
        sql = sql[: -1]

        count += 1
        print(f'第{count}次执行, sql: {sql}')
        cursor.execute(sql)
    conn.commit()
    conn.close()


if __name__ == '__main__':
    numbers = generate_random_numbers(start, end)
    insert_numbers(table_name, numbers)
