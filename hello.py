def main():
    print("Hello from learning-kafka!")


if __name__ == "__main__":
    a = [_ for _ in range(5000)]
    b = [_ for _ in range(1, 55)]

    for i, j in zip(a, b):
        print(i, j)
        if j - i == 1:
            continue
        else:
            print(f"Mismatch found: {i} and {j}")
            break
