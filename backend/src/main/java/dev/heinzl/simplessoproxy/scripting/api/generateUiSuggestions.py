import re

f = open("ScriptingApi.java", "r")
fileContentRaw = f.read()

fileContentRaw2 = fileContentRaw.replace('\n', ' ')
fileContentRaw3 = re.sub(' +', ' ', fileContentRaw2)

fileContent = re.search(r".*{(.*)}.*", fileContentRaw3).group(1)

methods = re.findall(r"\s*(.*?);", fileContent)

for method in methods:

    matches = re.search(r"((\w+)\((.*)\))", method)

    signature = matches.group(1)
    name = matches.group(2)
    parameters = matches.group(3)

    parameterSuggestions = []

    for parameter in parameters.split(','):
        print(parameter)
        print(re.search(r"(\w+)", parameter).group(1))
