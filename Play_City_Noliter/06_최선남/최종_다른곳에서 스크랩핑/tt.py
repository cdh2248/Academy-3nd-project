with open('search_list.txt', 'r') as f:
    search_list = f.read()
    f.close()
search_list_s=search_list.split('\n')


#
# for s in search_list_s:
#     print(s)

for c in range(len(search_list_s)):
    print(search_list_s[c])