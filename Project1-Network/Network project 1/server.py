import os
import socket
import threading

# Define a function to handle user CLI requests
def handle_cli_request():
    while True:
        user_input = input("Client request (cr, so, rt): ")
        # Check if the input command is one of the predefined commands
        if user_input in ['cr', 'so', 'rt']:
            # Dictionary mapping input commands to URL for redirection
            redirect_urls = {
                'cr': 'https://www.cornell.edu',
                'so': 'https://stackoverflow.com',
                'rt': 'https://ritaj.birzeit.edu'
            }
            # Prepare the HTTP header
            header = f'Header : HTTP/1.1 307 Temporary Redirect\nLocation: {redirect_urls[user_input]}\n\n'
            print(header)  # Print the header to the CLI.

# Function to handle incoming HTTP requests
def handle_http_request(connection, address):
    request = connection.recv(1024).decode('utf-8')  # Read data from the client
    # Split the request string
    string_list = request.split(' ')
    method = string_list[0]  # Extract the HTTP method
    requesting_file = string_list[1]  # Extract the requested file or command.
    # Print the full client request
    print('\nFull Client Request:\n', request)
    # Parse the file from the request URL, ignoring query parameters
    myfile = requesting_file.split('?')[0]
    myfile = myfile.lstrip('/')  # Remove the leading slash.

    # Default file assignments based on the request.
    if myfile == '' or myfile == 'en':
        myfile = 'main_en.html'
    elif myfile == 'ar':
        myfile = 'main_ar.html'
    # Check if the request is for one of the predefined redirects
  
        # Encode the response to bytes and send it
        final_response = header.encode('utf-8')
        connection.send(final_response)
        connection.close()
        return

    # Read the requested file.
    try:
        with open(myfile, 'rb') as file:
            response = file.read()
        header = 'HTTP/1.1 200 OK\n'  # Start preparing a success header

        if myfile.endswith(".jpg"):
            mimetype = 'image/jpeg'
        elif myfile.endswith(".png"):
            mimetype = 'image/png'
        elif myfile.endswith(".css"):
            mimetype = 'text/css'
        else:
            mimetype = 'text/html'
        # Append the type to the header.
        header += f'Content-Type: {mimetype}\n\n'
    # Handle the case where the requested file is not found.
    except FileNotFoundError:
        # Prepare a custom 404 error HTML response
        error_response = f"""<!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta http-equiv="X-UA-Compatible" content="IE=edge">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Error 404</title>
                        <style>
                            body {{ font-family: Arial, sans-serif; text-align: center; }}
                            .error-message {{ color: red; }}
                            .client-info {{ font-weight: bold; }}
                        </style>
                    </head>
                    <body>
                        <h1>HTTP/1.1 404 Not Found</h1>
                        <h2 class="error-message">The file is not found</h2>
                        <div class="client-info">
                            <p>1190546 - oday khallaf</p>
                            <p>1200609 - anwaar qasem </p>
                            <p>1200620 -  tala dabbagh </p>
                            <p>{address[0]}:{address[1]}</p>
                        </div>
                    </body>
                    </html>"""
        header = 'HTTP/1.1 404 Not Found\n\n'  # Prepare the error header.
        # Encode the error response to bytes.
        response = error_response.encode('utf-8')

    # Combine the header and the response content.
    final_response = header.encode('utf-8') + response
    # Send the final response to the client.
    connection.send(final_response)
    connection.close()  # Close the client connection.

# Set up the socket for the server.
HOST, PORT = '127.0.0.1', 9966  # Define port for the server
# Create a socket object supporting IPv4 and TCP protocols
my_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
my_socket.bind((HOST, PORT))  # Bind the socket to the host and port
my_socket.listen(1)  # Start listening for incoming connections

print('Serving on port', PORT)

# Start the CLI request
cli_thread = threading.Thread(target=handle_cli_request)
cli_thread.start()

# Start listening for network requests.
try:
    while True:
        # Accept an incoming connection
        connection, address = my_socket.accept()
        # Start a new thread to handle the HTTP request.
        thread = threading.Thread(target=handle_http_request, args=(connection, address))
        thread.start()
except KeyboardInterrupt:
    # Handle a keyboard interrupt to shut down the server gracefully.
    print("Server is shutting down.")
    my_socket.close()
