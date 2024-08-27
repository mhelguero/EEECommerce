# syntax=docker/dockerfile:1

ARG NODE_VERSION=20.13.1

FROM node:${NODE_VERSION}-alpine

# Use production node environment by default.
ENV NODE_ENV development

WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies and Vite globally
RUN npm ci

# Copy the rest of the application code
COPY . .

# Ensure the correct permissions for the copied files
RUN chown -R node:node /usr/src/app

# Switch to the non-root user
USER node

# Expose the port that the application listens on
EXPOSE 5173

# Run the application
ENTRYPOINT ["npm", "run", "dev"]
