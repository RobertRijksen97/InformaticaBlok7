import sys

print("TEST1")
print(2)


def functie1():
    print("TEST3")
    return "TEST4"


def main():
    print("TEST5")
    print(sys.argv[1])


def args():
    print(sys.argv[1])
    print(sys.argv[2])


if __name__ == '__main__':
    functie1()
    main()
    args()
    exit(55)

