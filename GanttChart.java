System.out.println("Gantt Chart");
        System.out.print(" ");
        for(int i=0;i<n;i++)
            System.out.print("___");
        System.out.print("\n|");
        for(int i=0;i<n;i++)
            System.out.print("  P" + (i+1) + "  |");
        System.out.println();
        System.out.print("|");
        for(int i=0;i<n;i++)
            System.out.print("__|");
        System.out.print("\n0");    
        for(int i=1;i<(n+1);i++)
        {
            if(gchart[i] <= 9)
                System.out.print("      " + gchart[i]);
            else if(gchart[i] > 9)
                System.out.print("     " + gchart[i]);
        }
        System.out.println("\n");
