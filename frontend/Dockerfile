# Use the official Node.js 21 image as the base image
FROM node:21

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json files to the working directory
COPY package*.json ./

# Install the project dependencies
RUN npm install

# Copy the rest of the project files to the working directory
COPY . .

# Expose the default port for the Nx project
EXPOSE 4200

# Start the development server
CMD ["npm", "run", "start"]
