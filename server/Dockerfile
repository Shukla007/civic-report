FROM node:20-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install --production

# Copy source files
COPY auth.js ./
COPY database.js ./
COPY database-mongo.js ./
COPY index.js ./
COPY config/ ./config/
COPY models/ ./models/

# Create uploads directory
RUN mkdir -p uploads

# Expose port
EXPOSE 5000

# Start the server
CMD ["node", "index.js"]
