import time
import sys
import re

# Function that parses the header of a CSV file
def parse_headerCSV(inputF, verbose):
    initial_headerCSV = inputF.readline()

    # Remove the '\n' from the header
    initial_headerCSV = re.sub(r'\n', r'', initial_headerCSV)

    headerCSV = []
    headersType = []

    # Match and give a type for each header
    while initial_headerCSV:
        if len(initial_headerCSV) == 0:
            break

        # Match a list header with aggregation function
        matchHEADER = re.findall(r'^(\w+{(\d+),?(\d*)?}::([\x41-\xFE]*))\b', initial_headerCSV)
        if matchHEADER:
            headerCSV.append(matchHEADER[0][0])
            # TYPE 0: List with defined size and aggregation function
            if matchHEADER[0][2] == '':
                headersType.append((0, int(matchHEADER[0][1]), matchHEADER[0][3]))
                initial_headerCSV = re.sub(r'^\w+{\d+,?\d*?}::[\x41-\xFE]*\b,?', r'', initial_headerCSV)
                for i in range(int(matchHEADER[0][1])):
                    initial_headerCSV = re.sub(r'^,', r'', initial_headerCSV)
            # TYPE 1: List with size range and aggregation function
            else:
                headersType.append((1, int(matchHEADER[0][1]), int(matchHEADER[0][2]), matchHEADER[0][3]))
                initial_headerCSV = re.sub(r'^\w+{\d+,?\d*?}::[\x41-\xFE]*\b,?', r'', initial_headerCSV)
                for i in range(int(matchHEADER[0][2])):
                    initial_headerCSV = re.sub(r'^,', r'', initial_headerCSV)
        
        else:
            # Match a list header
            matchHEADER = re.findall(r'^(\w+{(\d+),?(\d*)?})', initial_headerCSV)
            if matchHEADER:
                headerCSV.append(matchHEADER[0][0])
                # TYPE 2: List with defined size
                if not matchHEADER[0][2]:
                    headersType.append((2, int(matchHEADER[0][1])))
                    initial_headerCSV = re.sub(r'^\w+{\d+,?\d*?},?', r'', initial_headerCSV)
                    for i in range(int(matchHEADER[0][1])):
                        initial_headerCSV = re.sub(r'^,', r'', initial_headerCSV)
                # TYPE 3: List with size range
                else:
                    headersType.append((3, int(matchHEADER[0][1]), int(matchHEADER[0][2])))
                    initial_headerCSV = re.sub(r'^\w+{\d+,?\d*?},?', r'', initial_headerCSV)
                    for i in range(int(matchHEADER[0][2])):
                        initial_headerCSV = re.sub(r'^,', r'', initial_headerCSV)

            else:
                # Match a normal header
                matchHEADER = re.findall(r'^([^,]+)', initial_headerCSV)
                if matchHEADER:
                    headerCSV.append(matchHEADER[0])
                    # TYPE 4: Simple entry
                    headersType.append(4)
                    initial_headerCSV = re.sub(r'^([^,]+)?,?', r'', initial_headerCSV)
                
                else:
                    headerCSV.append("")
                    # TYPE -1: Invalid header
                    headersType.append(-1)
                    initial_headerCSV = re.sub(r'^,', r'', initial_headerCSV)

    if verbose == 'True':
        print('Verbose: enabled\n')
        print('Headers: Size = '+ str(len(headerCSV)))
        print(headerCSV)
        print('\nType of headers: Size = '+ str(len(headersType)))
        print(headersType)
        print('')
    else:
        print('Verbose: disabled')

    return (headerCSV, headersType)

# Writing in the json file
def writing_json(inputF, outputF, verbose, headerCSV, headersType):

    # Reading all the lines in the csv file
    inputTXT = inputF.readlines()
        
    outputF.write('[\n')

    for index, line in enumerate(inputTXT):
        outputF.write('\t{\n')
        line = re.sub(r'\n', r'', line)
        headers_index = 0

        if verbose == 'True':
            print('Line ('+ str(index) +'): '+ line)
        
        while line or headers_index <= len(headersType)-1:
            # TYPE -1: Invalid header
            if headersType[headers_index] == -1:
                line_aux = re.sub(r'^.*?,', r'', line)
                if len(line_aux) == len(line):
                    line = re.sub(r'^.*', r'', line)
                else:
                    line = line_aux

            # TYPE 4: Simple entry
            elif headersType[headers_index] == 4:
                outputF.write('\t\t"'+ headerCSV[headers_index] +'": "')

                row_match = re.match(r'^.+?[^,]*', line)
                row_match = row_match.group()
                outputF.write(row_match)
                line = re.sub(r'^.+?[^,]*,?', r'', line)

                outputF.write('"')

            # TYPE 3: List with size range or TYPE 2: List with defined size
            elif headersType[headers_index][0] == 3 or headersType[headers_index][0] == 2:
                header = re.findall(r'^[^{]+', headerCSV[headers_index])
                outputF.write('\t\t"'+ header[0] +'": [')

                if headersType[headers_index][0] == 3:
                    final_range = headersType[headers_index][2]
                else:
                    final_range = headersType[headers_index][1]

                for size_index in range(final_range):
                    row_match = re.match(r'^[^,]+', line)
                    if row_match:
                        row_match = row_match.group()
                        if size_index != 0:
                            outputF.write(',')
                        digit_match = re.match(r'-?\d+', row_match)
                        if digit_match:
                            outputF.write(row_match)
                        else:
                            outputF.write('"' + row_match + '"')
                        line = re.sub(r'^[^,]+,?', r'', line)
                    else:
                        line_aux = re.sub(r'^.*?,', r'', line)
                        if len(line_aux) == len(line):
                            line = re.sub(r'^.*', r'', line)
                        else:
                            line = line_aux

                outputF.write(']')

            # TYPE 1: List with size range and aggregation function or TYPE 0: List with defined size and aggregation function
            elif headersType[headers_index][0] == 1 or headersType[headers_index][0] == 0:
                header = re.findall(r'(^[^{]+).+::(\w+)', headerCSV[headers_index])
                outputF.write('\t\t"'+ header[0][0] + '_' + header[0][1] + '": ')

                if headersType[headers_index][0] == 1:
                    final_range = headersType[headers_index][2]
                    aggregation_function = headersType[headers_index][3]
                else:
                    final_range = headersType[headers_index][1]
                    aggregation_function = headersType[headers_index][2]

                all_numbers = []
                for size_index in range(final_range):
                    row_match = re.match(r'^[-+]?\d+', line)
                    if row_match:
                        row_match = row_match.group()
                        all_numbers.append(int(row_match))
                        line = re.sub(r'^[-+]?\d+,?', r'', line)
                    else:
                        line_aux = re.sub(r'^.*?,', r'', line)
                        if len(line_aux) == len(line):
                            line = re.sub(r'^.*', r'', line)
                        else:
                            line = line_aux
                total = 0
                # Aggregation function: AVERAGE
                if re.match(r'average\b', aggregation_function, re.IGNORECASE):
                    for i in all_numbers:
                        total += i
                    if len(all_numbers) > 0:
                        outputF.write(str(total/len(all_numbers)))
                    else:
                        outputF.write(str(total))

                # Aggregation function: COUNT
                elif re.match(r'count\b', aggregation_function, re.IGNORECASE):
                    outputF.write(str(len(all_numbers)))

                # Aggregation function: MAXIMUM
                elif re.match(r'maximum\b', aggregation_function, re.IGNORECASE):
                    if len(all_numbers) > 0:
                        outputF.write(str(max(all_numbers)))
                    else:
                        outputF.write(str(total))    

                # Aggregation function: MEDIAN
                elif re.match(r'median\b', aggregation_function, re.IGNORECASE):
                    all_numbers.sort()
                    if len(all_numbers) % 2 == 0:
                        while len(all_numbers) != 2 and len(all_numbers) != 0:
                            all_numbers = all_numbers[1:len(all_numbers)-1]
                        if len(all_numbers) > 0:
                            total = (all_numbers[0]+all_numbers[1])/2
                    else:
                        while len(all_numbers) != 1 and len(all_numbers) != 0:
                            all_numbers = all_numbers[1:len(all_numbers)-1]
                        if len(all_numbers) > 0:
                            total = all_numbers[0]
                    outputF.write(str(total))

                # Aggregation function: MINIMUM
                elif re.match(r'minimum\b', aggregation_function, re.IGNORECASE):
                    if len(all_numbers) > 0:
                        outputF.write(str(min(all_numbers)))
                    else:
                        outputF.write(str(total))

                # Aggregation function: MODE
                elif re.match(r'mode\b', aggregation_function, re.IGNORECASE):
                    if len(all_numbers) > 0 :
                        total = (max(set(all_numbers), key = all_numbers.count))
                    outputF.write(str(total))

                # Aggregation function: RANGE
                elif re.match(r'range\b', aggregation_function, re.IGNORECASE):
                    if len(all_numbers) > 0:
                        outputF.write(str(max(all_numbers)-min(all_numbers)))
                    else:
                        outputF.write(str(total))

                # Aggregation function: SUM
                elif re.match(r'sum\b', aggregation_function, re.IGNORECASE):
                    outputF.write(str(sum(all_numbers)))

                # Invalid aggregation function
                else:
                    outputF.write('"Invalid aggregation function"')

            if headers_index + 1 == len(headersType):
                outputF.write('\n')
                break
            elif headersType[headers_index] != -1:
                outputF.write(',\n')
            headers_index += 1

        if index + 1 != len(inputTXT):
            outputF.write('\t},\n')
        else:
            outputF.write('\t}\n]')

args = sys.argv[1:]
if len(args) < 1 or len(args) > 3 or len(args) == 2:
    print('Incorrect input.\nTry: main.py -help')
elif args[0] == '-help' or args[1] == '-help' or args[2] == '-help':
    print('*** CONVERTER: CSV -> JSON ***')
    print('Command: main.py <verbose> <path_csv_file> <out_filename>')
    print('Arguments:')
    print('\tVerbose: "True" "False"')
    print('\tPath_csv_file: "folder\\\input.csv" (always write two "\\") ')
    print('\tOut_filename: "output"')
else:
    verbose = args[0]
    inputF = open(args[1])
    output_filename = args[2] + '.json'

    # Create the output file
    outputF = open('files\out\\'+ output_filename, 'w+')

    # Start counting time
    start_time = time.time()
    print('Started conversion of CSV file to JSON format...')

    # Parsing csv header
    (headerCSV, headersType) = parse_headerCSV(inputF, verbose)

    # Writing to json file
    writing_json(inputF, outputF, verbose, headerCSV, headersType)

    # Stop counting time
    end_time = time.time()
    elapsed_time = end_time - start_time
    print('\nConversion finished in '+ str('{:.5f}'.format(elapsed_time)) +'s.')
