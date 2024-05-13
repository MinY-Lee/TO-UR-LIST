# Start from the official Neo4j image
FROM neo4j:5.19.0-community

# Add the modified custom-entrypoint.sh script to the image
COPY custom-entrypoint.sh /startup/custom-entrypoint.sh

# Grant execution permissions to the script
RUN chmod +x /startup/custom-entrypoint.sh /startup/docker-entrypoint.sh

# Set the custom script as the entrypoint
ENTRYPOINT ["tini", "-g", "--", "/startup/custom-entrypoint.sh"]



