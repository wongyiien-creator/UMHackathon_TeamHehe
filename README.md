# Business Optimizer
# Video Link
https://drive.google.com/drive/folders/1gxFX2DcgufHC5ZqzPOFNmoCKbIRzXMjp?usp=sharing 

in case the google drive link is not working:
https://youtu.be/HndruStaQuw

# Documents Link
You also can find the necessary documents (.pdf) in the Documents folder in github.
### Product Requirement
https://1drv.ms/w/c/8cfe547694fc94fd/IQC-AuTqQ1qxTrY2Nz5sPwJBAergl1ET4xy2o4bkmliPai0?e=10Wrdk 
### System Analysis
https://1drv.ms/w/c/8cfe547694fc94fd/IQCzFEln24OYSJFm2jJ5LPUBAVmA3nzf7FU1otTxyHxtX1M?e=YxmKa5
### Sample Testing
https://1drv.ms/w/c/8cfe547694fc94fd/IQCd8xboqWuQSZFMX0DzBLBRAVIdcSO9ggFrGuF4Ls6ejmc?e=k7A8IS 
### Presentation Deck
https://canva.link/hje7or3ff35lwt9 

# Project Description
In a saturated marketplace where consumer attention spans are declining quickly, businesses might struggle to establish a trustworthy brand that meet evolving customers’ needs consistently. Traditional branding often fails to capture attention of customer immediately, leading to a gap between a company’s core message and its target audience’s perception.

# Solution Summary
- An AI-driven strategic engine that transforms raw market data and internal data into executable business strategy. 
- By automating the transition from market analysis to designing marketing mix strategy
- Enable user to upload their data so marketing mix fit their context
- Compare two different marketing strategies and provide its recommendations

# System Functionalities
### Market Analysis
Interpreted structured and instructed data provided by the user to understand the market change and potential opportunities, and explain with context.
### Marketing Mix Proposal
After market analysis, it will provide a contextual marketing mix strategy for the business to use. Each decision is also explained based on the context to ensure the plan is not outdated.
### Vertex AI Search
Get context information based on region and industry. This is to provide context for market mix comparison. 
### Marketing Mix Comparison
Compare two different marketing strategies and pick the best one with reasoning. Further improvement will also be made.
### Save Data
Able to save market data into database so that market analysis will have more data to work with.

# Technical Stack
- Frontend: Android Framework API
- Backend: FastAPI
- Database: Firebase - Firestore
- Cloud/Deployment: Cloud Run Deployment

# External Dependencies
- Z.AI API: Serves as the core reasoning engine
- Firestore: Database for user and marketing data
- Vertex AI Search: Search for regional market data
- Google Cloud Run: Host  the Python backend

