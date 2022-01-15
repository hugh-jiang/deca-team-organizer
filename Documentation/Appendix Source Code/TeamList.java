/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DecaTeamOrganizer;

import java.util.ArrayList;
import java.io.*;

/**
 * TeamList Class that stores lists of Member objects and provides backend functionality such as filtering members, searching, deleting, importing, exporting, etc. 
 * @author Hugh Jiang
 */
public class TeamList {
    
    // Constants
    final static int ARRAYLIST_DEFAULT_CAPACITY = 125;
    final static int[] GRADE_LIST = {9, 10, 11, 12};
    final static String[] SUBTEAMS = {"Select a Subteam", "Finance", "Marketing", "Business Administration", "Principles", "Hospitality & Tourism", "Writtens", "None"};
    final static String STORAGE_FILE = "storage.csv";
    final static int MIN_SEARCH_QUERY_LENGTH = 3;
    
    // Instance variables
    private ArrayList<Member> allMembers;
    private ArrayList<Integer> filteredIndex;
    private ArrayList<Member> filteredMembers;
    private ArrayList<Member> searchedMembers;
    private ArrayList<String> partialSearchQueries;
    private int numMembers;
    private boolean sortByFirstName;
    
    /**
     * Constructor initializes default instance variables
     */
    public TeamList() {
        initialize();
    }
    
    
    // Instance Methods
    
    /**
     * Method to initialize instance variables, for use in constructor and to reset the instance to default values
     */
    private void initialize() {
        allMembers = new ArrayList<>(ARRAYLIST_DEFAULT_CAPACITY);
        filteredIndex = new ArrayList<>(ARRAYLIST_DEFAULT_CAPACITY);
        filteredMembers = new ArrayList<>(ARRAYLIST_DEFAULT_CAPACITY);
        searchedMembers = new ArrayList<>(ARRAYLIST_DEFAULT_CAPACITY);
        partialSearchQueries = new ArrayList<>();
        numMembers = 0;
        
        // Set default sorting to use first name
        sortByFirstName = true;
    }
    
    /**
     * Recursive Insertion sort algorithm to sort allMembers ArrayList by alphabetical first name
     * For use in the public sort() method
     * @param allMembers the ArrayList of Members (must be an instance variable of the TeamList class) that needs to be sorted
     * @param arraySize the size of the unsorted part of the array (when initially calling the method, set to the size of the array)
     */
    private void sortFirstName(ArrayList<Member> allMembers, int arraySize) {
        
        // Base case: when the array is only one element
        if (arraySize <= 1) {
            return;
        }
        
        sortFirstName(allMembers, arraySize-1);
        
        // get the last member
        Member lastElement = allMembers.get(arraySize-1);
        // get the index of the element before lastElement
        int previousIndex = arraySize-2;
        
        // while previousIndex>=0 and Member at previousIndex > lastElement (the next Element), swap larger element further back in array
        // sort by alphabetical order based on first name, last name 
        while (previousIndex >= 0 && (allMembers.get(previousIndex).getFullName()).compareTo(lastElement.getFullName()) > 0) {
            allMembers.set(previousIndex + 1, allMembers.get(previousIndex));
            previousIndex--;
        }
        
        allMembers.set(previousIndex+1, lastElement);
    }
    
    /**
     * Recursive Insertion sort algorithm to sort allMembers ArrayList by alphabetical last name, 
     * For use in the public sort() method
     * @param allMembers the ArrayList of Members (must be an instance variable of the TeamList class) that needs to be sorted
     * @param arraySize the size of the unsorted part of the array (when initially calling the method, set to the size of the array)
     */
    private void sortLastName(ArrayList<Member> allMembers, int arraySize) {
        // Base case: when the array is only one element
        if (arraySize <= 1) {
            return;
        }
        
        sortLastName(allMembers, arraySize-1);
        
        // get the last member
        Member lastElement = allMembers.get(arraySize-1);
        // get the index of the element before lastElement
        int previousIndex = arraySize-2;
        
        // while previousIndex>=0 and Member at previousIndex > lastElement (the next Element), swap larger element further back in array
        // sort by alphabetical order based on first name, last name 
        while (previousIndex >= 0 && (allMembers.get(previousIndex).getFullNameLastFirst()).compareTo(lastElement.getFullNameLastFirst()) > 0) {
            allMembers.set(previousIndex + 1, allMembers.get(previousIndex));
            previousIndex--;
        }
        
        allMembers.set(previousIndex+1, lastElement);
    }
    
    /**
     * Write csv file to export members to the storage file. 
     * Code adapted from: https://stackabuse.com/reading-and-writing-csvs-in-java/
     */
    public void exportMembers() {
        
        try {
            FileWriter writer = new FileWriter(STORAGE_FILE);
            
            // First delete existing file before writing new file
            File file = new File(STORAGE_FILE);
            file.delete();
        
            // Write file header
            writer.append("First Name");
            writer.append(",");
            writer.append("Last Name");
            writer.append(",");
            writer.append("Email");
            writer.append(",");
            writer.append("Grade");
            writer.append(",");
            writer.append("Subteam");
            writer.append(",");
            writer.append("Event ID");

            // Write rest of file
            for (int i = 0; i < allMembers.size(); i++) {
                writer.append("\n");
                writer.append(allMembers.get(i).getFirstName() + ",");
                writer.append(allMembers.get(i).getLastname() + ",");
                writer.append(allMembers.get(i).getEmail() + ",");
                writer.append(allMembers.get(i).getGrade() + ",");
                writer.append(allMembers.get(i).getSubteam() + " ,");
                writer.append(allMembers.get(i).getEventID() + " ");
            }

            writer.flush();
            writer.close();
        }
        
        catch (IOException error) {
            // Catch error so program doesn't crash
            System.out.println("Error in exporting file");
        }
    }
    
    
    
    /**
     * Private recursive binary search that searches for a member with a matching first name. 
     * Method is to be called in binarySearchMember method. 
     * @param arr the ArrayList of Members (must be an instance variable of the TeamList class) that needs to be searched 
     * @param firstName the String containing the first name of the member being searched
     * @param low the lower index bound of the binary search (set initially to 0)
     * @param high the upper index bound of the binary search (set initially to array length - 1)
     * @return the index of the first found member with a matching first name
     */
    private int binarySearchFirstName(ArrayList<Member> arr, String firstName, int low, int high) {
        int foundIndex;
        int midIndex;
        
        // Clean firstName argument
        String key = firstName.trim();
        key = firstName.toUpperCase().charAt(0) + firstName.substring(1);
        
        // Base case, name not found
        if (low > high) {
            return -1;
        }
        else {
            midIndex = (low + high) / 2;
            String currentFirstName = arr.get(midIndex).getFirstName().trim();
            
            if (currentFirstName.equalsIgnoreCase(key)) {
                foundIndex = midIndex;
            } 
            else {
                // If the key is less than the current index in binary search
                if (currentFirstName.compareTo(key) < 0) {
                    foundIndex = binarySearchFirstName(arr, key, midIndex+1, high);
                } 
                // If the key is less than the current index in binary search
                else {
                    foundIndex = binarySearchFirstName(arr, key, low, midIndex-1);
                }
            }
            
            return foundIndex;
        }
    }
    
    /**
     * Private recursive binary search that searches for a member with a matching last name. 
     * Method is to be called in binarySearchMember method. 
     * @param arr the ArrayList of Members (must be an instance variable of the TeamList class) that needs to be searched 
     * @param lastName the String containing the last name of the member being searched
     * @param low the lower index bound of the binary search (set initially to 0)
     * @param high the upper index bound of the binary search (set initially to array length - 1)
     * @return the index of the first found member with a matching last name
     */
    private int binarySearchLastName(ArrayList<Member> arr, String lastName, int low, int high) {
        int foundIndex;
        int midIndex;
        
        // Clean firstName argument
        String key = lastName.trim();
        key = lastName.toUpperCase().charAt(0) + lastName.substring(1);
        
        // Base case, name not found
        if (low > high) {
            return -1;
        }
        else {
            midIndex = (low + high) / 2;
            String currentLastName = arr.get(midIndex).getLastname().trim();
            
            if (currentLastName.equalsIgnoreCase(key)) {
                foundIndex = midIndex;
            } 
            else {
                // If the key is less than the current index in binary search
                if (currentLastName.compareTo(key) < 0) {
                    foundIndex = binarySearchLastName(arr, key, midIndex+1, high);
                } 
                // If the key is less than the current index in binary search
                else {
                    foundIndex = binarySearchLastName(arr, key, low, midIndex-1);
                }
            }
            
            return foundIndex;
        }
    }
    
    /**
     * Binary search for a member based on either their first name or their last name, depending on sorting settings that are set
     * @param arr the ArrayList of Members to be searched (must be an instance variable of this class)
     * @return the index of the found name
     */
    public int binarySearchMember(ArrayList<Member> arr, Member m) {
        
        if (sortByFirstName) {
            String firstName = m.getFirstName();
            return binarySearchFirstName(arr, firstName, 0, arr.size()-1);
        }
        else {
            String lastName = m.getLastname();
            return binarySearchLastName(arr, lastName, 0, arr.size()-1);
        }
    }
    
    
    
    // Mutator Methods
    
    /**
     * Mutator method to add a member
     * @param m the Member to be added to the team
     */
    public void addMember(Member m) {
        
        // Add member to the allMembers ArrayList
        allMembers.add(m);
        
        // Increment Counter
        numMembers++;
        
        // Sort (by insertion)
        sort();
        
        /* 
        Note: add member to other subteams only when requested in their respective mutator methods: 
        this way, we do not have to continually re-sort multiple arrays
        */
        
        // Set list of filtered members to default (no filter)
        filteredMembers = allMembers;
    }
    
    /**
     * Delete all members in the team by re-initializing all instance variables
     */
    public void deleteAll() {
        initialize();
    }
    
    /**
     * Method that searches for a member and then deletes them from the main allMembers ArrayList.
     * Uses a binary search to search for a matching name. If the first matching name is not the right member, 
     * linear search from that both directions of the binary search index until the correct member is found, then delete.
     * @param m the Member to be deleted from the team 
     * @return boolean indicating if member was successfully deleted (if deleted then return true, else return false)
     */
    public boolean deleteMember(Member m) {
        boolean deleted = false;
        int startSearchIndex;
        
        // First use binary search to find a member with the same name as the member we are trying to delete
        startSearchIndex = binarySearchMember(allMembers, m);
        
        // Then check if the member is the same. if the same, then delete and return true
        if (m.equals(allMembers.get(startSearchIndex))) {
            allMembers.remove(startSearchIndex);
            deleted = true;

            // Decrement members counter
            numMembers--;
        }
        // If the member is not the same (i.e. has same name but different grade or event etc), start from 
        // the index of the found member and linear search for the member that is the same
        else {
            
            for (int i = 0; i < allMembers.size(); i++) {

                // Linear search of positive direction from starting index
                if (startSearchIndex + i < allMembers.size()) {
                    if (m.equals(allMembers.get(startSearchIndex + i))) {
                        allMembers.remove(startSearchIndex + i);
                        deleted = true;

                        // Decrement members counter
                        numMembers--;

                        break;
                    }
                }

                // Linear search of negative direction from starting index
                if (startSearchIndex - 1 >= 0) {
                    if (m.equals(allMembers.get(startSearchIndex - i))) {
                        allMembers.remove(startSearchIndex - i);
                        deleted = true;

                        // Decrement members counter
                        numMembers--;

                        break;
                    }
                }
            }
        }
        
        return deleted;
    }
    
    /**
     * Method that deletes a member by index from the main allMembers ArrayList
     * @param indexToDelete the index of the Member that is to be deleted (from the main allMembers ArrayList)
     * @return boolean value indicating whether the member was successfully deleted (if deleted then return true, else return false)
     */
    public boolean deleteMember(int indexToDelete) {
        if (indexToDelete < allMembers.size()) {
            allMembers.remove(indexToDelete);
            numMembers--;
            return true;
        }
        return false;
    }
    
    /**
     * Filter team based on grade input
     * @param grade the grade that is to be filtered
     */
    public void filterByGrade(int grade) {
        ArrayList<Member> temp = new ArrayList<>(numMembers);
        ArrayList<Integer> tempIndex = new ArrayList<>(numMembers);
        
        for (int i = 0; i < filteredMembers.size(); i++) {
            if (filteredMembers.get(i).getGrade() == grade) {
                temp.add(filteredMembers.get(i));
                tempIndex.add(filteredIndex.get(i));
            }
        }
        
        filteredIndex = tempIndex;
        filteredMembers = temp;
    }
    
    /**
     * Filter team based on subteam input
     * @param subteam the String containing the subteam that is to be filtered
     */
    public void filterBySubteam(String subteam) {
        ArrayList<Member> temp = new ArrayList<>(numMembers);
        ArrayList<Integer> tempIndex = new ArrayList<>(numMembers);
        
        /* 
        If the selected subteam is "None" (last element in the SUBTEAMS array) then set 
        subteam to an empty String to search for members with no subteam
        Note: an empty String is the default value for no subteam in the Member class  
        */
        if (subteam.equals(SUBTEAMS[SUBTEAMS.length-1])) {
            subteam = "";
        }
        
        for (int i = 0; i < filteredMembers.size(); i++) {
            if (filteredMembers.get(i).getSubteam().equalsIgnoreCase(subteam.trim())) {
                temp.add(filteredMembers.get(i));
                tempIndex.add(filteredIndex.get(i));
            }
        }
        
        filteredIndex = tempIndex;
        filteredMembers = temp;
    }
    
    /**
     * Read csv or txt file to import Members into the main team list (allMembers).
     * Code adapted from: https://stackabuse.com/reading-and-writing-csvs-in-java/
     * @param fileName the String containing the path and name of the imported file
     */
    public void importMembers(String fileName) {
        BufferedReader buffer;
        String row;
        int rowCounter = 0;
        
        try {
            buffer = new BufferedReader(new FileReader(fileName));
            
            while((row = buffer.readLine()) != null) {
                // After a comma in a file row, add element to rowData array
                String rowData[] = row.split(",");
                rowCounter++;
                
                // Add member data as long as row is not the first row (which is the header row)
                if (rowCounter != 1) {
                    if (rowData.length >= 5) {
                        addMember(new Member(rowData[0].trim(), rowData[1].trim(), rowData[2].trim(), 
                                Integer.valueOf(rowData[3].trim()), rowData[4].trim(), rowData[5].trim()));
                    }
                    else {
                        addMember(new Member(rowData[0].trim(), rowData[1].trim(), rowData[2].trim(), Integer.valueOf(rowData[3].trim())));
                    }
                }
            }
            buffer.close();
        }
        catch (IOException error) {
            // Error handling
            System.out.println("Troubleshoot file reading error");
        }
    }
    
    /**
     * Mutator method to reset filters
     */
    public void resetFilters() {
        // Set filtered list to be the same as the main list
        filteredMembers = allMembers;
        
        // Set filtered indexes to a new array containing a list of integers from 1, 2, 3, ... numMembers
        filteredIndex = new ArrayList<>(numMembers);
        for (int i = 0; i < numMembers; i++) {
            filteredIndex.add(i);
        }
    }
    
    /**
     * Mutator method to reset searches. This is a private method because searches are 
     * automatically reset after each search so layering search upon search is not possible,
     * thus it will only be privately called within TeamList and not in Gui
     */
    private void resetSearch() {
        searchedMembers = new ArrayList<>();
        partialSearchQueries = new ArrayList<>();
    }
    
    /**
     * Mutator method to set whether TeamList sorts by first name or by last name
     * @param x the boolean variable: if true, sort by first name. If false, sort by last name
     */
    public void setSortByFirstName(boolean x) {
        sortByFirstName = x;
    }
    
    /**
     * Method to sort allMembers alphabetically, either by first name or 
     * last name depending on the boolean flag sortByFirstName
     */
    public void sort() {
        if (sortByFirstName) {
            sortFirstName(allMembers, numMembers);
        }
        else {
            sortLastName(allMembers, numMembers);
        }
    }
    
    
    
    // Accessor Methods
    
    /**
     * Accessor method to get a Member at a specified index in the main list
     * @param index the index where the member is located
     * @return the Member at the specified index in the main list
     */
    public Member get(int index) {
        return allMembers.get(index);
    }
    
    /**
     * Accessor method to get indexes of filtered members
     * @return ArrayList containing the index of the filtered members in the main list
     */
    public ArrayList<Integer> getFilteredIndex() {
        return filteredIndex;
    }
    
    /**
     * Accessor method to get filtered team list
     * @return filtered ArrayList of Members
     */
    public ArrayList<Member> getFilteredList() {
        return filteredMembers;
    }
    
    /**
     * Accessor method to get members on the team list
     * @return ArrayList containing the Members of the team
     */
    public ArrayList<Member> getMembers() {
       return allMembers;
    }
    
    /**
     * Accessor method to get number of total members in list
     * @return 
     */
    public int getNumMembers() {
        return numMembers;
    }
    
    /**
     * Searches the filteredList for a String query and returns an ArrayList of members containing that query in their name, email, or eventID
     * The search will match partial queries as well as full queries (i.e. John will match a search for "Johnny" and vice versa)
     * @param query the String search query. Must be at least 3 characters long
     * @return the ArrayList of Members that match the search (or partial search) in order of relevance
     */
    public ArrayList<Member> getSearch(String query) {
        // First, reset any previous searches
        resetSearch();
        
        // Do not proceed if we don't meet the minumum search query length
        if (query.length() < MIN_SEARCH_QUERY_LENGTH) {
            return (new ArrayList<>());
        }
        
        Member tempMember;
        String currentQuery;
        
        // Populate the partialSearchQueries ArrayList with the partial queries to be searched for
        findAllSubstrings(query, 0);
        
        for (int queryIndex = 0; queryIndex < partialSearchQueries.size(); queryIndex++) {
            
            for (int i = 0; i < filteredMembers.size(); i++) {
                
                tempMember = filteredMembers.get(i);
                currentQuery = partialSearchQueries.get(queryIndex);
                
                // Check if the search query is found in the member's name, email, event (retrieved from Member.getSearchString())
                if ((tempMember.getSearchString().toLowerCase()).indexOf(currentQuery.toLowerCase()) != -1) {

                    // Add tempMember to the searchedMembers list if they are not already in the list from a previous iteration of the method
                    if (!searchedMembers.contains(tempMember)) {
                        searchedMembers.add(tempMember);
                    }
                }
            }
        }
        return searchedMembers;
    }
    
    /**
     * Recursive method that finds all the substrings (partial queries) in a String search query and adds them to a storage ArrayList. 
     * Substrings less than 3 characters in length are excluded. The substrings will be added in order of descending length.
     * For example, if we call findAllSubstrings("ABCDE", 0) we'll add {"ABCDE", "ABCD", "BCDE", "ABC", "BCD", "CDE"} to the storage list
     * 
     * @param query the String search query that we have to find the substrings for
     * @param n the integer that shortens the substring length <b>(when initially calling the method, default to n = 0)</b>. Ex: if n=2 then then we find all the substrings that are 2 characters shorter than the original query
     */
    private void findAllSubstrings(String query, int n) {
        
        // When n=0, the search query is the entire input String (this is the first case to run)
        if (n == 0) {
            // Add original query to the list of partial queries
            partialSearchQueries.add(query);
            
            // Recurse with n=n+1 to decrease the length of the substring, increasing the number of substrings found
            findAllSubstrings(query, n+1);
        }
        
        if (n > 0 && query.length() > MIN_SEARCH_QUERY_LENGTH) {
            // Find the length of the partial substrings, then the number of total partial substrings for this value of n
            int partialQueryLength = query.length() - n;
            int numPartialQueries = n + 1;
            
            // Get all the substrings of length partialQueryLength in the original query, then add to partialSearchQueries ArrayList
            for (int i = 0; i < numPartialQueries; i++) {
                String partialQuery = query.substring(i, partialQueryLength+i);
                
                partialSearchQueries.add(partialQuery);
            }
            
            /*Stop recursion if the next partial query will less than the minimum query length
              Note that if the current length is MIN_SEARCH_QUERY_LENGTH, we want to stop the recursion, 
              because the next partial queries will be less than the allowed min length
            */
            if (partialQueryLength <= MIN_SEARCH_QUERY_LENGTH) {
                return;
            }
            // Otherwise continue recursion
            else {
                findAllSubstrings(query, n+1);
            }
        }
    }
    
    /*
    private void search() {
        Member tempMember = new Member();
        // Search for the member and add to list
            for (int i = 0; i < filteredMembers.size(); i++) {
                tempMember = filteredMembers.get(i);
                
                // Check if the search query is found in the member's name, email, event
                if (query.indexOf(tempMember.getSearchString()) != -1) {
                    
                    // Add tempMember to the searchedMembers list if they are not already in the list from a previous iteration of the method
                    if (!searchedMembers.contains(tempMember)) {
                        searchedMembers.add(tempMember);
                    }
                }
            }
    }*/
}
