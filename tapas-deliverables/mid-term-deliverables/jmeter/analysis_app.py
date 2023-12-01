import streamlit as st
import pandas as pd
import plotly.graph_objects as go
from plotly.subplots import make_subplots
import plotly.express as px
import os


st.title("JMeter Performance Test Results")
st.subheader("ASSE, Group 5, St.Gallen")

#directory = r"C:\Users\061246848\Documents\ASSE5\JMeter_Test\Reports\Tasks\Local"

task_selection = st.selectbox("Select Test Scenario:", options=["Tasks", "All", "Roster"], index=0)
location_selection = st.selectbox("Select Location:", options=["Local", "VM"], index=0)
directory = os.path.join(
    "Reports",
    task_selection,
    location_selection
)

st.info(f"Selected report path: {directory}") 





# Load CSV files from the directory
# csv_files = [f for f in os.listdir(directory) if f.endswith('.csv')]


cleaned_cache = st.checkbox('Tested with Cleaned Cache')

# Load CSV files from the directory based on the checkbox state
if cleaned_cache:
    csv_files = [f for f in os.listdir(directory) if f.endswith('clear.csv')]
else:
    csv_files = [f for f in os.listdir(directory) if f.endswith('.csv') and not f.endswith('clear.csv')]




# Merging all CSV files into one DataFrame
all_data = []
for file in csv_files:
    file_path = os.path.join(directory, file)
    data = pd.read_csv(file_path)
    data['File'] = file  # Optionally add a 'File' column to keep track of the source file
    all_data.append(data)

try:
    merged_df = pd.concat(all_data)
except ValueError as e:
    st.warning("No Reports found")
    st.stop()  # Stop the app if no reports are found

# Sorting merged_df by '# Samples' in ascending order
merged_df = merged_df.sort_values(by='# Samples', ascending=True)

# Creating subplots with secondary Y-axis
fig = make_subplots(specs=[[{"secondary_y": True}]])

# Adding scatter plot for Throughput vs # Samples
fig.add_trace(
    go.Scatter(x=merged_df['# Samples'], y=merged_df['Throughput'], mode='lines+markers', name='Throughput'),
    secondary_y=False,
)

# Adding scatter plots for specified columns
for col in ["Average", "Median", "90% Line", "95% Line", "99% Line"]:
    fig.add_trace(
        go.Scatter(x=merged_df['# Samples'], y=merged_df[col], mode='lines+markers', name=col),
        secondary_y=True,
    )

st.subheader("Test Results Trends over different #Samples")
# Updating layout for better readability, labeling, and adding a range slider
fig.update_layout(
    xaxis_title='# Samples',
    yaxis_title='Throughput',
    yaxis2_title='Response [ms]',
    xaxis_rangeslider_visible=True  # Adding a range slider
)

st.plotly_chart(fig)

# User selection of data point
st.subheader("See Results Table for specific # Samples")
selected_sample = st.selectbox("Select # Samples:", options=merged_df['# Samples'].unique())

# Display the corresponding row from the DataFrame
selected_row = merged_df[merged_df['# Samples'] == selected_sample]
st.write(selected_row)


# Adding explanations in an expander
with st.expander("Explanations"):
    st.markdown("""
    - **Throughput**: The number of requests that are processed per time unit (e.g., requests per second). Higher throughput indicates better performance.
    - **Average**: The mean response time. It's the total response time divided by the number of requests.
    - **Median**: The median response time. 50% of the requests are processed within this time.
    - **90% Line**: 90% of the requests are processed within this time. It's a measure of the tail latency.
    - **95% Line**: 95% of the requests are processed within this time. It's also a measure of the tail latency.
    - **99% Line**: 99% of the requests are processed within this time, indicating the performance for nearly all the requests.
    """)





# Filtering for rows where Label is 'TOTAL'
total_df = merged_df[merged_df['Label'] == 'TOTAL']

# Creating a grouped bar plot for Received KB/sec and Sent KB/sec vs # Samples
fig_bar = px.bar(
    total_df, 
    x='# Samples', 
    y=['Received KB/sec', 'Sent KB/sec'],
    title='Received KB/sec and Sent KB/sec vs # Samples (All Reports)',
    labels={'value': 'KB/sec', '# Samples': '# Samples'},
    height=400,
)

# Updating the bar mode to group the bars instead of stacking them
fig_bar.update_layout(barmode='group')

# Adding line traces to connect the bars
fig_bar.add_trace(
    go.Scatter(
        x=total_df['# Samples'], 
        y=total_df['Received KB/sec'], 
        mode='lines+markers', 
        name='Received KB/sec Line',
        line=dict(color='navy')  # Set line color to grey
    )
)

fig_bar.add_trace(
    go.Scatter(
        x=total_df['# Samples'], 
        y=total_df['Sent KB/sec'], 
        mode='lines+markers', 
        name='Sent KB/sec Line',
        line=dict(color='lightblue')  # Set line color to grey
    )
)

st.plotly_chart(fig_bar)




