#!/bin/bash

# Memastikan kita berada di direktori Git
if [ ! -d ".git" ]; then
    echo "Error: Ini bukan repositori Git."
    exit 1
fi

# Meminta pesan commit dari pengguna
read -p "Masukkan pesan commit: " commit_message

# Menambahkan semua perubahan
git add .

# Melakukan commit
git commit -m "$commit_message"

echo "Commit berhasil dengan pesan: $commit_message"

# Meminta nama branch untuk push
read -p "Masukkan nama branch untuk push (default: main): " branch_name
branch_name=${branch_name:-main}  # Jika tidak ada input, gunakan 'main'

# Melakukan push
git push origin "$branch_name"

if [ $? -eq 0 ]; then
    echo "Push berhasil ke branch: $branch_name"
else
    echo "Error: Push gagal."
fi